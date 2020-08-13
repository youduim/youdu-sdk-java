package im.youdu.sdk.client;

import com.google.gson.JsonObject;
import im.youdu.sdk.encrypt.AESCrypto;
import im.youdu.sdk.entity.FileInfo;
import im.youdu.sdk.entity.YDApp;
import im.youdu.sdk.entity.YdApi;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.FileIOException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.util.Helper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

public class CustomMenuClient {

    private int buin;
    private String host;
    private String appId;
    private String appAeskey;

    private AESCrypto crypto;
    private AppTokenClient tokenClient;

    public CustomMenuClient(YDApp app) {
        this.buin = app.getBuin();
        this.host = app.getHost();
        this.appId = app.getAppId();
        this.appAeskey = app.getAppAesKey();
        this.crypto = new AESCrypto(appId, appAeskey);
        this.tokenClient = new AppTokenClient(buin, host, appId, appAeskey);
    }

    /**
     * 会话窗口自定菜单，下载zip文件并保存到dir目录
     * @param forwardId 转发id
     * @param dir 保存目录
     * @return
     * @throws IOException
     * @throws HttpRequestException
     * @throws FileIOException
     * @throws AESCryptoException
     * @throws ParamParserException
     */
    public FileInfo downloadChatMsgZipFileAndSave(String forwardId, String dir) throws IOException, HttpRequestException, FileIOException, AESCryptoException, ParamParserException {
        byte[] bytes = this.downloadChatMsgZipFile(forwardId);

        FileInfo fileInfo = new FileInfo();
        fileInfo.setData(bytes);
        fileInfo.setName(forwardId + ".zip");
        fileInfo.setMediaId(forwardId);
        fileInfo.setPath(Helper.saveFile(fileInfo, dir));

        return fileInfo;
    }

    /**
     * 自定义菜单，会话窗口自定义菜单，下载zip文件
     *
     * @param forwardId 转发Id
     * @return
     * @throws HttpRequestException
     * @throws FileIOException
     * @throws AESCryptoException
     * @throws ParamParserException
     */
    public byte[] downloadChatMsgZipFile(String forwardId) throws HttpRequestException, FileIOException, AESCryptoException, ParamParserException {

        JsonObject mediaIdJson = new JsonObject();
        mediaIdJson.addProperty("forward_id", forwardId);
        String cipherId = this.crypto.encrypt(Helper.utf8Bytes(mediaIdJson.toString()));

        JsonObject param = new JsonObject();
        param.addProperty("buin", this.buin);
        param.addProperty("appId", this.appId);
        param.addProperty("encrypt", cipherId);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpRsp = null;

        String url = String.format("%s%s%s?accessToken=%s", YdApi.SCHEME, this.host, YdApi.API_DOWNLOAD_COMPLEX_MSG_ZIP, this.tokenClient.getToken());

        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(param.toString(), ContentType.APPLICATION_JSON);
            entity.setContentEncoding("UTF-8");
            httpPost.setEntity(entity);

            httpRsp = httpClient.execute(httpPost);
            InputStream inputStream = httpRsp.getEntity().getContent();

            byte[] encryptBuffer = Helper.readInputStream(inputStream);
            String encryptContent = Helper.utf8String(encryptBuffer);
            return this.crypto.decrypt(encryptContent);
        } catch (IOException e) {
            throw new HttpRequestException(0, e.getMessage(), e);
        } finally {
            Helper.close(httpRsp);
            Helper.close(httpClient);
        }
    }
}
