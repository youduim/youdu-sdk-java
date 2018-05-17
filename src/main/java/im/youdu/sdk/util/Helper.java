package im.youdu.sdk.util;

import com.google.gson.*;
import im.youdu.sdk.encrypt.AESCrypto;
import im.youdu.sdk.encrypt.Signature;
import im.youdu.sdk.entity.*;
import im.youdu.sdk.exception.*;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class Helper {

    public static JsonObject parseJson(String json) throws ParamParserException {
        try {
            return new JsonParser().parse(json).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            throw new ParamParserException(e.getMessage(), e);
        }
    }

    public static int getInt(String name, JsonObject object) {
        JsonElement element = object.get(name);
        if (element == null || !element.isJsonPrimitive()) {
            return 0;
        }
        return element.getAsInt();
    }

    public static long getLong(String name, JsonObject object) {
        JsonElement element = object.get(name);
        if (element == null || !element.isJsonPrimitive()) {
            return 0;
        }
        return element.getAsLong();
    }

    public static float getFloat(String name, JsonObject object) {
        JsonElement element = object.get(name);
        if (element == null || !element.isJsonPrimitive()) {
            return 0;
        }
        return element.getAsFloat();
    }

    public static double getDouble(String name, JsonObject object) {
        JsonElement element = object.get(name);
        if (element == null || !element.isJsonPrimitive()) {
            return 0;
        }
        return element.getAsDouble();
    }

    public static Number getNumber(String name, JsonObject object) {
        JsonElement element = object.get(name);
        if (element == null || !element.isJsonPrimitive()) {
            return 0;
        }
        return element.getAsNumber();
    }

    public static BigInteger getBigInteger(String name, JsonObject object) {
        JsonElement element = object.get(name);
        if (element == null || !element.isJsonPrimitive()) {
            return new BigInteger("0");
        }
        return element.getAsBigInteger();
    }

    public static String getString(String name, JsonObject object) {
        JsonElement element = object.get(name);
        if (element == null || !element.isJsonPrimitive()) {
            return "";
        }
        return element.getAsString();
    }

    public static boolean getBoolean(String name, JsonObject object) {
        JsonElement element = object.get(name);
        if (element == null || !element.isJsonPrimitive()) {
            return false;
        }
        return element.getAsBoolean();
    }

    public static JsonObject getObject(String name, JsonObject object) {
        JsonElement element = object.get(name);
        if (element == null || !element.isJsonObject()) {
            return null;
        }
        return element.getAsJsonObject();
    }

    public static JsonArray getArray(String name, JsonObject object) {
        JsonElement element = object.get(name);
        if (element == null || !element.isJsonArray()) {
            return null;
        }
        return element.getAsJsonArray();
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] readInputStream(InputStream in) throws IOException {
        byte[] content;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            int length = in.available();
            if (length == 0) {
                length = 1024;
            }
            byte[] buffer = new byte[length];
            int readLength;
            while ((readLength = in.read(buffer)) > 0) {
                out.write(buffer, 0, readLength);
            }
            content = out.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            close(out);
        }
        return content;
    }

    public static JsonObject postJson(String url, String json) throws ParamParserException, HttpRequestException {
        System.out.println(String.format("param: %s", json));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpRsp = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            entity.setContentEncoding("UTF-8");
            httpPost.setEntity(entity);

            httpRsp = httpClient.execute(httpPost);
            parseHttpStatus(httpRsp);
            JsonObject jsonResult = readHttpResponse(httpRsp);
            parseApiStatus(jsonResult);
            return jsonResult;
        } catch (IOException e) {
            throw new HttpRequestException(0, e.toString(), e);
        } finally {
            Helper.close(httpRsp);
            Helper.close(httpClient);
        }
    }

    public static void getUrlV1(String url) throws HttpRequestException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpRsp = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpRsp = httpClient.execute(httpGet);
            parseHttpStatus(httpRsp);
        } catch (IOException e) {
            throw new HttpRequestException(0, e.toString(), e);
        } finally {
            Helper.close(httpRsp);
            Helper.close(httpClient);
        }
    }

    public static JsonObject getUrlV2(String url) throws ParamParserException, HttpRequestException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpRsp = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpRsp = httpClient.execute(httpGet);
            parseHttpStatus(httpRsp);
            JsonObject jsonResult = readHttpResponse(httpRsp);
            parseApiStatus(jsonResult);
            return jsonResult;
        } catch (IOException e) {
            throw new HttpRequestException(0, e.toString(), e);
        } finally {
            Helper.close(httpRsp);
            Helper.close(httpClient);
        }
    }

    public static void parseHttpStatus(CloseableHttpResponse httpRsp) throws HttpRequestException {
        StatusLine status = httpRsp.getStatusLine();
        if (status.getStatusCode() != 200) {
            throw new HttpRequestException(status.getStatusCode(), status.getReasonPhrase(), null);
        }
    }

    public static void parseApiStatus(JsonObject jsonObject) throws HttpRequestException {
        int code = getInt("errcode", jsonObject);
        String message = getString("errmsg", jsonObject);
        if (code != 0) {
            throw new HttpRequestException(code, message, null);
        }
    }

    public static String readAndEncryptFile(AESCrypto crypto, String path) throws FileIOException, AESCryptoException {
        FileInputStream file = null;
        try {
            file = new FileInputStream(path);
            byte[] buffer = Helper.readInputStream(file);
            return crypto.encrypt(buffer);
        } catch (IOException e) {
            throw new FileIOException(e.getMessage(), e);
        } finally {
            Helper.close(file);
        }
    }

    public static byte[] decryptAndWriteFile(AESCrypto crypto, CloseableHttpResponse httpRsp,
                                             String name, String destDir)
            throws FileIOException, AESCryptoException, ParamParserException {

        HttpEntity rspEntity = httpRsp.getEntity();
        InputStream in = null;
        FileOutputStream outfile = null;
        try {
            in = rspEntity.getContent();
            outfile = new FileOutputStream(destDir + File.separator + name);
            byte[] encryptBuffer = Helper.readInputStream(in);
            String encryptContent = Helper.utf8String(encryptBuffer);
            byte[] fileContent = crypto.decrypt(encryptContent);
            outfile.write(fileContent, 0, fileContent.length);
            return fileContent;
        } catch (IOException e) {
            throw new FileIOException(e.getMessage(), e);
        } finally {
            Helper.close(in);
            Helper.close(outfile);
        }
    }

    public static JsonObject readHttpResponse(CloseableHttpResponse httpRsp) throws ParamParserException, HttpRequestException {
        HttpEntity rspEntity = httpRsp.getEntity();
        InputStream in = null;
        try {
            in = rspEntity.getContent();
            byte[] buffer = Helper.readInputStream(in);
            String result = Helper.utf8String(buffer);
            return parseJson(result);
        } catch (IOException e) {
            throw new HttpRequestException(0, e.getMessage(), e);
        } finally {
            Helper.close(in);
        }
    }

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }

    public static String utf8String(byte[] buffer) throws ParamParserException {
        try {
            return new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ParamParserException(e.getMessage(), e);
        }
    }

    public static byte[] utf8Bytes(String content) throws ParamParserException {
        try {
            return content.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ParamParserException(e.getMessage(), e);
        }
    }

    public static List<Map<String,MessageBody>> parseComplexMsgBody(JsonArray jArray) {
        List<Map<String, MessageBody>> list = new ArrayList<Map<String, MessageBody>>();
        if (null == jArray || jArray.size()==0) {
            return list;
        }

        for (JsonElement e : jArray) {
            JsonObject jObj = e.getAsJsonObject();
            if (jObj.has("txt")) { //文本消息
                String content = Helper.getString("txt",jObj);
                TextBody body = new TextBody(content);
                Map m = new HashMap();
                m.put(Const.Message_Session_Type_Text, body);
                list.add(m);
                continue;
            }
            if (jObj.has("url")) { //隐式链接
                String url = Helper.getString("url",jObj);
                String title = Helper.getString("title",jObj);
                LinkBody body = new LinkBody(url, title, 0);
                Map m = new HashMap();
                m.put(Const.Message_Session_Type_Link, body);
                list.add(m);
                continue;
            }
            if (jObj.has("image_id")) { //图片消息
                long size = Helper.getLong("size",jObj);
                String name = Helper.getString("name",jObj);
                String mediaId = Helper.getString("media_id",jObj);
                ImageBody body = new ImageBody(name,mediaId,size);
                Map m = new HashMap();
                m.put(Const.Message_Session_Type_Image, body);
                list.add(m);
                continue;
            }
            System.out.println("unkown msg:" + jObj.toString());
        }
        return list;
    }

    public static boolean signatureIsValid(Map<String, String> params, String token, String encrypt){
        boolean yes = false;
        try {
            String nonce = params.get("nonce");
            String timestamp = params.get("timestamp");
            String fromSignature = params.get("msg_signature");
            String mySignature = Signature.generateSignature(token, timestamp, nonce, encrypt);
            yes = (mySignature.equals(fromSignature));
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return yes;
    }

    public static String readFileNameFromPath(String path){
        path = path.trim();
       int idx = path.lastIndexOf("/");
       if(idx==-1){
           idx = path.lastIndexOf("\\");
       }
       return path.substring(idx+1);
    }

    public static String saveFile(FileInfo info, String dir) throws FileIOException {
        FileOutputStream outfile = null;
        String finalPath = dir + File.separator + info.getName();
        try {
            outfile = new FileOutputStream(finalPath);
            outfile.write(info.getData(), 0, info.size());
            return finalPath;
        } catch (IOException e) {
            throw new FileIOException(e.getMessage(), e);
        } finally {
            Helper.close(outfile);
        }
    }

    public static byte[] readFile(String path) throws FileIOException {
        FileInputStream file = null;
        try {
            file = new FileInputStream(path);
            byte[] buffer = readInputStream(file);
            return buffer;
        } catch (IOException e) {
            throw new FileIOException(e.getMessage(), e);
        } finally {
            Helper.close(file);
        }
    }

    public static boolean isEmpty(String str){
        return null == str || "".equals(str.trim());
    }
}
