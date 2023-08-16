java版有度SDK说明
====================
SDK类介绍
--------------------
|类名|使用说明|
| -------------  |-------------
| YDApp          | 有度应用对象   
| AppClient      | 应用消息发送      
| OrgClient      | 组织架构管理       
| GroupClient    | 群管理
| SysMsgClient   | 系统消息发送
| IdentifyClient | 单点登录身份认证
| SessionClient  | 会话管理及会话消息发送

***
## 目录
* [YDApp](#YDApp)
* [AppClient](#AppClient)
* [OrgClient](#OrgClient)
* [GroupClient](#GroupClient)
* [SysMsgClient](#SysMsgClient)
* [IdentifyClient](#IdentifyClient)
* [SessionClient](#SessionClient)
***

### YDApp 有度应用对象
    有度应用对象是所有接口都需要用到的基础对象

***
#### 构建YDApp
    import im.youdu.sdk.entity
    YDApp ydApp = new YDApp(int buin, String srvHost, String appName, String appId, String token, String appAesKey)
    
    buin: 企业总机号
    srvHost: 有度服务器地址，例如 10.0.0.168:7080
    appName: 应用名称
    appId: 应用ID
    token: 应用回调设置的token，没有就传空串
    appAesKey: 应用回调设置的EncodingAESKey
***



### AppClient
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

#### downloadFileAndSave  下载并保存文件
    appClient.downloadFileAndSave(String mediaId, String dir)
    mediaId:  文件ID
    dir:  需要保存的文件夹路径

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
    构建LinkBody对象：
    import im.youdu.sdk.entity
    LinkBody body = new LinkBody(String url, String title, int action);
    url: 需要点击打开的网页链接
    title: 隐式链接显示的标题
    action：1：有度客户端会在url追加上有度身份认证token

#### sendExlinkMsg  给应用发送外链消息
    appClient.sendExlinkMsg(String toUser, String toDept, ExlinkBody exLink)
    exLink: 外链对象
    
    构建外链对象
    ExlinkBodyCell cell = new ExlinkBodyCell(String title, String url, String digest, String mediaId)
    title: 外链标题
    url: 需要点击打开的网页链接
    digest：外链摘要
    mediaId: 外链需要显示的图片，可为空
    例如：
    ExlinkBodyCell cell1 = new ExlinkBodyCell("有度","https://youdu.cn","有度官网", "");
    ExlinkBodyCell cell2 = new ExlinkBodyCell("有度下载","https://youdu.cn/download.html","有度下载", "");
    ExlinkBody body = new ExlinkBody();
    body.addCell(cell1);
    body.addCell(cell2);

#### sendMpnewsMsg  给应用发送图文消息
    appClient.sendMpnewsMsg(String toUser, String toDept, MpnewsBody mpnews)
    mpnews: 图文对象
    
#### setAppNotice  设置应用角标(待办数通知)
    appClient.setAppNotice(String account, int count, String tips)
    account: 目标用户账号
    count: 角标值(待办数)
    tips: 弹窗提醒，不需要就传空串
***

### OrgClient
--------------------
#### Dept
	import im.youdu.sdk.entity.Dept;
	Dept ydDept = new Dept(int id, String name, int parentId, int sortId, String alias)
	
	id：部门编号
	name: 部门名称
	parentId: 父部门编号, 根部门ID为0
	sortId: 部门排序
	alias: 部门别名

#### 构建OrgClient
    import im.youdu.sdk.client
    OrgClient orgClient = new OrgClient(ydApp)

#### createDept  创建部门
    int deptId = orgClient.createDept(Dept dept)
    dept: 部门信息
	deptId: 部门编号

#### updateDept  修改部门
    orgClient.updateDept(Dept dept)
    dept: 部门信息

#### getDept  获取部门
    Dept dept = orgClient.getDept(int deptId)
    deptId:  部门编号

#### deleteDept  删除部门
    orgClient.deleteDept(int deptId)
    deptId:  部门编号

#### listDeptChildren  获取部门直属子部门列表
    List<Dept> depts = orgClient.listDeptChildren(int deptId)
    deptId: 部门编号。根部门ID为0
 
#### listDeptSelfAndChildren  获取部门及直属子部门列表
    List<Dept> depts = orgClient.listDeptSelfAndChildren(int deptId)
    deptId: 部门编号。根部门ID为0

#### getDeptIdByAlias  根据别名获取部门ID
    int deptId = orgClient.downloadFileAndSave(String alias)
    alias:  部门别名
    deptId: 部门编号

#### listAliasDept  获取部门别名列表
    List<AliasDept> depts = orgClient.listAliasDept()
    depts:  图片ID

	import im.youdu.sdk.entity.AliasDept
	int deptId; //部门编号
	String alias; //别名
***

### SysMsgClient
--------------------
#### 构建SysMsgClient
    import im.youdu.sdk.client;
    
    int buin = 707168;
    String host = "127.0.0.1:7080";
    String appId = "sysMsgAssistant";
    String appAESKey = "nHff0+CrZRd0jg/o+b2BpLVVI0XzgOkasRaz50k80KE=";
    YDApp app = new YDApp(buin, host, "", appId, "", appAESKey);
    SysMsgClient sysmsgClient = new SysMsgClient(app);

#### sendSysMsg  发送系统消息
    sysmsgClient.sendSysMsg(String toUser, String toDept, SysMsgBody sysMsg)
    sysMsg: 系统消息对象
    
    构建SysMsgBody对象：系统消息支持文本和隐式链接
    import im.youdu.sdk.client;
    
    SysMsgBody sysMsg = new SysMsgBody();
    sysMsg.setTitle("有度即时通"); //设置系统消息标题
    sysMsg.addTextBody("欢迎使用有度即时通："); //添加文字内容
    sysMsg.addLinkBody("https://youdu.cn","有度官网",0);//添加隐式链接
***

### IdentifyClient
    有度身份认证
--------------------
#### 构建IdentifyClient对象
    import im.youdu.sdk.client;
    
    String host = "127.0.0.1:7080";
    IdentifyClient identifyClient = new IdentifyClient(host);
	
#### idetify 身份认证获取用户信息
    UserInfo userInfo = identifyClient.idetify(String ydToken)
    ydToken: 有度客户端提供的认证token
    userInfo：根据ydToken获取到的有度用户信息
    
    account: 账号
    chsName: 姓名
    gender: 性别, 0:男; 1:女
    mobile: 手机号码
    phone: 座机号码
    email: 邮箱账号
***

### GroupClient 群管理
--------------------
#### 构建GroupClient对象
    import im.youdu.sdk.client;
    
    int buin = 707168;
    String host = "127.0.0.1:7080";
    String appId = "sysOrgAssistant";
    String appAESKey = "n76ut0qxPWozXbMxGMt8s9pgxUZKUxu/GJ5R5dz+u4g=";
    YDApp app = new YDApp(buin, host, "", appId, "", appAESKey);
    GroupClient groupClient = new GroupClient(app);
	
#### createGroup 创建群
    groupClient.createGroup(String groupName)
    groupName: 群名称

#### listAllGroups 获取所有群列表
    List<GroupBase> groups = groupClient.listAllGroups()
    GroupBase: 群基本信息
	
    id: 群ID
    name: 群名称

#### listUserGroups 获取用户的所有群列表
    List<GroupBase> groups = groupClient.listUserGroups(String userId)
    userId: 用户账号

#### deleteGroup 删除群
    groupClient.deleteGroup(String groupId)
    groupId: 群ID

#### updateGroupName 更新群名称
    groupClient.updateGroupName(String groupId, String groupName)
    groupId: 群ID
    groupName: 群名称

#### groupInfo 获取群信息
    groupClient.groupInfo(String groupId)
    groupId: 群ID

#### addGroupMember 增加群成员
    groupClient.addGroupMember(String groupId, String[] addUserList)
    groupId: 群ID
    addUserList: 要增加的群成员账号列表

#### delGroupMember 删除群成员
    groupClient.delGroupMember(String groupId, String[] delUserList)
    groupId: 群ID
    delUserList: 要删除的群成员账号列表

#### isGroupMember 判断用户是否群成员
    groupClient.isGroupMember(String groupId, String userId)
    groupId: 群ID
    userId: 用户账号
***
	

### SessionClient
--------------------
#### 构建SessionClient对象
    import im.youdu.sdk.client;
    
    int buin = 707168;
    String host = "127.0.0.1:7080";
    String appId = "sysOrgAssistant";
    String appAESKey = "n76ut0qxPWozXbMxGMt8s9pgxUZKUxu/GJ5R5dz+u4g=";
    YDApp app = new YDApp(buin, host, "", appId, "", appAESKey);
    SessionClient sessionClient = new SessionClient(app);
 
#### SessionInfo 会话对象
    sessionId: 会话ID
    title: 会话标题
    owner: 会话创建者
    version: 会话版本号
    type: 会话类型
    member: 会话成员账号列表
	
    只需创建多人会话，单人会话不需要创建。
    群会话ID就是创建群的时候返回的群ID，所以如果需要给群发送消息，可以通过GroupClient拉取群列表获取到相应群的ID即可。
 
#### createSession 创建会话
    SessionInfo session = sessionClient.createSession(SessionCreateBody body)
    SessionCreateBody:
    title: 会话标题
    creator: 创建者账号
    member: 成员账号列表
    type: 会话类型，不需要填写，默认就是multi(多人会话)

#### getSession 获取会话
    sessionClient.getSession(String sessionId)
    sessionId: 会话ID

#### updateSessionTitle 更新会话标题
    sessionClient.updateSessionTitle(String sessionId,String opUser,String title)
    sessionId: 会话ID
    opUser: 修改会话标题的用户账号
    title: 会话标题

#### updateSession 更新会话
    sessionClient.updateSession(SessionUpdateBody body)
    SessionUpdateBody:
    sessionId: 会话ID
    opUser: 修改会话标题的用户账号
    title: 会话标题
    addMember: 增加的成员列表
    delMember: 删除的成员列表

#### sendSingleTextMsg 发送单人会话文字消息
    sessionClient.sendSingleTextMsg(String fromUser, String toUser, String content)
    fromUser: 消息发送人账号
    toUser: 消息接收者账号
    content: 文本消息

#### sendSingleImgMsg 发送单人会话图片消息
    sessionClient.sendSingleImgMsg(String fromUser, String toUser, String imgPath)
    fromUser: 消息发送人账号
    toUser: 消息接收者账号
    imgPath: 图片绝对路径

#### sendSingleImgMsgWithMediaId 发送单人会话图片消息
    sessionClient.sendSingleImgMsgWithMediaId(String fromUser, String toUser, String imgId)
    fromUser: 消息发送人账号
    toUser: 消息接收者账号
    imgId: 图片ID

#### sendSingleFileMsg 发送单人会话文件消息
    sessionClient.sendSingleFileMsg(String fromUser, String toUser, String filePath)
    fromUser: 消息发送人账号
    toUser: 消息接收者账号
    filePath: 文件绝对路径

#### sendSingleFileMsgWithFileId  发送单人会话文件消息
    sessionClient.sendSingleFileMsgWithFileId(String fromUser, String toUser, String fileId)
    fromUser: 消息发送人账号
    toUser: 消息接收者账号
    fileId: 文件ID

#### sendSingleVoiceMsg  发送单人会话语音消息
    sessionClient.sendSingleVoiceMsg(String fromUser, String toUser, byte[] voiceData)
    fromUser: 消息发送人账号
    toUser: 消息接收者账号
    voiceData: 语音文件二进制数据

#### sendSingleVideoMsg  发送单人会话视频消息
    sessionClient.sendSingleVideoMsg(String fromUser, String toUser, byte[] videoData)
    fromUser: 消息发送人账号
    toUser: 消息接收者账号
    videoData: 视频文件二进制数据

#### sendSessionTextMsg  发送多人会话文字消息
    sessionClient.sendSessionTextMsg(String fromUser, String sessionId, String content)
    fromUser: 消息发送人账号
    sessionId: 会话ID
    content: 文本消息

#### sendSessionImgMsg 发送多人会话图片消息
    sessionClient.sendSessionImgMsg(String fromUser, String sessionId, String imgPath)
    fromUser: 消息发送人账号
    sessionId: 会话ID
    imgPath: 图片绝对路径

#### sendSessionImgMsgWithImgId 发送多人会话图片消息
    sessionClient.sendSessionImgMsgWithImgId(String fromUser, String sessionId, String imgId)
    fromUser: 消息发送人账号
    sessionId: 会话ID
    imgId: 图片ID

#### sendSessionFileMsg 发送多人会话文件消息
    sessionClient.sendSessionFileMsg(String fromUser, String sessionId, String filePath)
    fromUser: 消息发送人账号
    sessionId: 会话ID
    filePath: 文件绝对路径
	

#### sendSessionFileMsgWithFileId 发送多人会话文件消息
    sessionClient.sendSessionFileMsgWithFileId(String fromUser, String sessionId, String fileId)
    fromUser: 消息发送人账号
    sessionId: 会话ID
    fileId: 文件ID

#### sendSessionVoiceMsg 发送多人会话语音消息
    sessionClient.sendSessionVoiceMsg(String fromUser, String sessionId, byte[] voiceData)
    fromUser: 消息发送人账号
    toUser: 消息接收者账号
    voiceData: 语音文件二进制数据

#### sendSessionVideoMsg 发送多人会话视频消息
    sessionClient.sendSessionVideoMsg(String fromUser, String sessionId, byte[] videoData)
    fromUser: 消息发送人账号
    toUser: 消息接收者账号
    videoData: 视频文件二进制数据

#### downloadMsgZipFile  获取会话消息zip文件的byte[]内容
    sessionClient.downloadMsgZipFile(String fileId)
    fileId:  zip文件id

#### downloadMsgZipFileAndSave  下载会话消息的zip文件并保存到dir目录
    sessionClient.downloadMsgZipFileAndSave(String fileId, String dir)
    fileId: zip文件id
    dir:  需要保存的文件夹路径