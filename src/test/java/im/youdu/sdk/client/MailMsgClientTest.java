package im.youdu.sdk.client;

import im.youdu.sdk.entity.Const;
import im.youdu.sdk.entity.EmailBody;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import junit.framework.TestCase;

import java.util.Calendar;

public class MailMsgClientTest extends TestCase {
    private static final int BUIN = 707168; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String EXMAIL_APP_AESKEY = "T+dB7Lhy5LJO52yYbPXyEuI7uARUBQbvX0lUSKmNqpw="; // 请填写企业邮应用的EncodingaesKey

    private MailMsgClient mailMsgClient;

    public MailMsgClientTest() throws Exception {
        mailMsgClient = new MailMsgClient(BUIN, YDSERVER_HOST, EXMAIL_APP_AESKEY);
    }

    //发送新邮件消息
    public void testSendNewMailMsgToUser() throws ParamParserException, HttpRequestException, AESCryptoException {
        EmailBody mailMsg = new EmailBody();
        mailMsg.setAction(Const.Mail_Msg_New);
        mailMsg.setSubject("测试邮件");
        mailMsg.setFromUser("test1");
        String toUser = "test2";
        mailMsgClient.sendMailMsg(toUser, "", mailMsg);
    }

    //发送新邮件消息
    public void testSendNewMailMsgToEmail() throws ParamParserException, HttpRequestException, AESCryptoException {
        EmailBody mailMsg = new EmailBody();
        mailMsg.setAction(Const.Mail_Msg_New);
        mailMsg.setSubject("测试邮件");
        mailMsg.setFromUser("test1");
        String toEmail = "test2@test.com";
        mailMsgClient.sendMailMsg("", toEmail, mailMsg);
    }

    //发送未读邮件数消息
    public void testSendUnreadMailMsgToUser() throws ParamParserException, HttpRequestException, AESCryptoException {
        long timex = Calendar.getInstance().getTimeInMillis();
        int unreadCount = 10;
        String toUser = "test1|test2";
        mailMsgClient.sendUnreadCountMailMsg(toUser, "", unreadCount, timex);
    }

    //发送未读邮件数消息
    public void testSendUnreadMailMsgToEmail() throws ParamParserException, HttpRequestException, AESCryptoException {
        long timex = Calendar.getInstance().getTimeInMillis();
        int unreadCount = 9;
        String toEmail = "test1@test.com";
        mailMsgClient.sendUnreadCountMailMsg("", toEmail, unreadCount, timex);
    }
}
