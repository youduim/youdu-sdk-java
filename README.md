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
#### 构建YDApp
    import im.youdu.sdk.entity
    YDApp ydApp = new YDApp(int buin, String srvHost, String appName, String appId, String token, String appAesKey)
    
    buin: 企业总机号
    srvHost: 有度服务器地址，比如 10.0.0.168:7080
    appName: 应用名称，可以设置空串
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

#### uploadFile  上传文件

#### uploadFileWithBytes  上传文件

#### uploadVoice  上传语音

#### uploadVideo  上传视频
