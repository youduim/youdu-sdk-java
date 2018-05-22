java版有度服务SDK说明
====================
1、SDK类介绍
--------------------
|类名|使用说明|
| -------------  |-------------
| YDApp          | 有度应用对象      
| AppClient      | 应用消息发送      
| OrgClient      | 组织架构管理       
| GroupClient    | 群管理            
| IdentifyClient | 单点登录身份认证      
| SessionClient  | 会话管理及会话消息发送 

YDApp 有度应用对象
--------------------
### 构建YDApp
    import im.youdu.sdk.entity
    YDApp ydApp = new YDApp(int buin, String srvHost, String appName, String appId, String token, String appAesKey)
    buin: 企业总机号
    srvHost: 有度服务器地址，比如 10.0.0.168:7080
    appName: 有度应用名称，可以设置空串
    appId:有度应用ID
    token:有度应用回调设置的token
    appAesKey:有度应用回调设置的EncodingAESKey

AppClient
--------------------
### 构建AppClient
    import im.youdu.sdk.client
    AppClient appClient = new AppClient(ydApp)
### uploadImage 上传图片
    appClient.uploadImage(String name, String path)
    name: 图片名称
    path: 图片绝对路径。如果name为空就从path读取
<br>
uploadImageWithBytes
  上传图片
<br>
uploadFile
  上传文件
<br>
uploadFileWithBytes
  上传文件
<br>
uploadVoice
  上传语音
<br>
uploadVideo
  上传视频
<br>
downloadFile
  下载文件
<br>
downloadFileAndSave
  下载并保存文件
<br>
downloadImage
  下载图片
<br>
downloadImageAndSave
  下载并保存图片
<br>
sendTextMsg
  给应用发送文本消息
<br>
sendFileMsg
  给应用发送文件消息
<br>
sendFileMsgWithMediaId
  给应用发送文件消息
<br>
sendImageMsg
  给应用发送图片消息
<br>
sendImageMsgWithMediaId
  给应用发送图片消息
<br>
sendLinkMsg
  给应用发送隐式链接消息
<br>
sendExlinkMsg
  给应用发送外链消息
<br>
sendMpnewsMsg
  给应用发送图文消息
<br>
sendSysMsg
  给应用发送系统消息
<br>
setAppNotice
  给应用推送待办数
<br>
popWindow
  给应用推送弹窗
<br>
