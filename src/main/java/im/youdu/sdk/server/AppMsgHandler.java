package im.youdu.sdk.server;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import im.youdu.sdk.client.AppClient;
import im.youdu.sdk.entity.Const;
import im.youdu.sdk.entity.ReceiveMessage;
import im.youdu.sdk.entity.YDApp;
import im.youdu.sdk.exception.GeneralEntAppException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.exception.SignatureException;
import im.youdu.sdk.util.Helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

public class AppMsgHandler implements HttpHandler {
    private YDApp app;
    private AppClient appClient;
    private IReceiveYdMsg receiver;

    public AppMsgHandler(YDApp app, IReceiveYdMsg receiver) {
        this.app = app;
        this.receiver = receiver;
        this.appClient = new AppClient(app);
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        System.out.println(String.format(Const.Log_Receive_Msg, this.app.getAppName()));
        InputStream in = t.getRequestBody();
        byte[] reqData = Helper.readInputStream(in);
        Helper.close(in);
        final Map<String, String> reqParams = Helper.queryToMap(t.getRequestURI().getQuery());
        new HandlerThread(this.app,this.appClient,this.receiver,reqData,reqParams).start();
        String response = "ok";
        t.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
        OutputStream out = t.getResponseBody();
        out.write(response.getBytes());
        out.close();
    }


    public YDApp getApp() {
        return app;
    }

    public void setApp(YDApp app) {
        this.app = app;
    }

    public AppClient getAppClient() {
        return appClient;
    }

    public void setAppClient(AppClient appClient) {
        this.appClient = appClient;
    }

    public IReceiveYdMsg getReceiver() {
        return receiver;
    }

    public void setReceiver(IReceiveYdMsg receiver) {
        this.receiver = receiver;
    }

    class HandlerThread extends Thread{
        private YDApp app;
        private AppClient appClient;
        private IReceiveYdMsg receiver;

        private byte[] reqData;
        private Map<String,String> reqParam;

        public HandlerThread(YDApp app, AppClient appClient, IReceiveYdMsg receiver,  byte[] reqData, Map<String,String> reqParam) {
            this.app = app;
            this.receiver = receiver;
            this.appClient = appClient;

            this.reqData = reqData;
            this.reqParam = reqParam;
        }

        @Override
        public void run() {
            this.handlerReq();
        }

        private void handlerReq(){
            try {
                String content = Helper.utf8String(reqData);
                JsonObject reqJson = Helper.parseJson(content);
                int buin = Helper.getInt("toBuin", reqJson);
                String appId = Helper.getString("toApp", reqJson);
                String encrypt = Helper.getString("encrypt", reqJson);
                if(buin != this.app.getBuin()){
                    System.out.println(String.format(Const.Log_Receive_Msg_Error_Invalid_BuinUnequal, this.app.getAppName(),buin, this.app.getBuin()));
                    return;
                }
                if(!appId.equals(this.app.getAppId())){
                    System.out.println(String.format(Const.Log_Receive_Msg_Error_Invalid_AppIdUnequal, this.app.getAppName(),appId, this.app.getAppId()));
                    return;
                }
                boolean fromYD = Helper.signatureIsValid(reqParam, this.app.getToken(), encrypt);
                if(!fromYD){
                    System.out.println(String.format(Const.Log_Receive_Msg_Error_Invalid_Signature,this.app.getAppName()));
                    return;
                }
                ReceiveMessage msg = this.appClient.decrypt(encrypt);
                this.receiver.receive(msg);
            } catch (SignatureException e) {
                e.printStackTrace();
            } catch (ParamParserException e) {
                e.printStackTrace();
            } catch (GeneralEntAppException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
