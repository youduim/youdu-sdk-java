java版有度SDK说明
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
#### 构建YDApp
    import im.youdu.sdk.entity
    YDApp ydApp = new YDApp(int buin, String srvHost, String appName, String appId, String token, String appAesKey)
    
    buin: 企业总机号
    srvHost: 有度服务器地址，比如 10.0.0.168:7080
    appName: 应用名称
    appId: 应用ID
    token: 应用回调设置的token
    appAesKey: 应用回调设置的EncodingAESKey
***
AppClient
--------------------
#### 构建AppClient
    import im.youdu.sdk.client
    AppClient appClient = new AppClient(ydApp)
#### uploadImage  上传图片
    appClient.uploadImage(String name, String path)
    name: 图片名称
    path: 图片绝对路径。如果name为空就从path读取
#### uploadImageWithBytes  上传图片
    appClient.uploadImageWithBytes(String name, byte[] data)
    name:  图片名称
    data:  图片二进制数据

#### uploadFile  上传文件
    appClient.uploadFile(String name, String path)
    name: 文件名称
    path: 文件绝对路径。如果name为空就从path读取

#### uploadFileWithBytes  上传文件
    appClient.uploadFileWithBytes(String name, byte[] data)
    name:  文件名称
    data:  文件二进制数据

#### uploadVoice  上传语音
    appClient.uploadVoice(String name, byte[] data)
    name:  语音文件名称
    data:  语音文件二进制数据

#### uploadVideo  上传视频
    appClient.uploadVideo(String name, byte[] data)
    name:  视频文件名称
    data:  视频文件二进制数据
    
#### downloadFile  下载文件
    appClient.downloadFile(String mediaId)
    mediaId:  文件ID

#### downloadFileAndSave  下载并保存文件
    appClient.downloadFileAndSave(String mediaId, String dir)
    mediaId:  文件ID
    dir:  需要保存的文件夹路径

#### downloadImage  下载图片
    appClient.downloadImage(String mediaId)
    mediaId:  图片ID

#### downloadImageAndSave  下载并保存图片
    appClient.downloadImageAndSave(String mediaId, String dir)
    mediaId:  图片ID
    dir:  需要保存的文件夹路径

#### sendTextMsg  给应用发送文本消息
    appClient.sendTextMsg(String toUser, String toDept, String content)
    toUser: 消息接收者，多个接收者用|分隔
    toDept: 消息接收部门，多个部门用|分隔
    content: 文字内容

#### sendFileMsg  给应用发送文件消息
    appClient.sendFileMsg(String toUser, String toDept, String fileName, String filePath)
    fileName: 文件名称
    filePath: 文件绝对路径

#### sendFileMsgWithMediaId  给应用发送文件消息
    appClient.sendFileMsgWithMediaId(String toUser, String toDept, String mediaId)
    mediaId: 文件ID

#### sendImageMsg  给应用发送图片消息
    appClient.sendImageMsg(String toUser, String toDept, String imageName, String imagePath)
    fileName: 图片名称
    filePath: 图片绝对路径

#### sendImageMsgWithMediaId  给应用发送图片消息
    appClient.sendImageMsgWithMediaId(String toUser, String toDept, String mediaId)
    mediaId: 文件ID

#### sendLinkMsg  给应用发送隐式链接消息
    appClient.sendLinkMsg(String toUser, String toDept, LinkBody link)
    link: 隐式链接对象

#### sendExlinkMsg  给应用发送外链消息
    appClient.sendExlinkMsg(String toUser, String toDept, ExlinkBody exLink)
    exLink: 外链对象

#### sendMpnewsMsg  给应用发送图文消息
    appClient.sendMpnewsMsg(String toUser, String toDept, MpnewsBody mpnews)
    mpnews: 图文对象

#### sendSysMsg  发送系统消息
    appClient.sendSysMsg(String toUser, String toDept, SysMsgBody sysMsg)
    sysMsg: 系统消息对象
