package im.youdu.sdk.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import im.youdu.sdk.entity.YDApp;
import im.youdu.sdk.exception.ServiceException;

import java.io.IOException;
import java.net.InetSocketAddress;


public class AppServer implements HttpHandler {
    private int port;
    private HttpServer server;

    public AppServer(int srvPort) throws ServiceException {
        try {
            this.port = srvPort;
            this.server = HttpServer.create(new InetSocketAddress(srvPort), 0);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void setAppHandler( String uri, YDApp app,IReceiveYdMsg receiver){
        AppMsgHandler h = new AppMsgHandler(app,receiver);
        this.server.createContext(uri,h);
    }

    public void start() {
        this.server.start();
        System.out.println(String.format("YD AppServer linsten on: %d",this.port));
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        System.out.println("unsupport......");
    }
}
