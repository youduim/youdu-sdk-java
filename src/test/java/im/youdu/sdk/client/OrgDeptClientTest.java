package im.youdu.sdk.client;

import im.youdu.sdk.entity.Dept;
import im.youdu.sdk.entity.YDApp;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import junit.framework.TestCase;

import java.util.List;

public class OrgDeptClientTest extends TestCase {
    private static final int BUIN = 707168; // 请填写企业总机号码
    private static final String YDSERVER_HOST = "127.0.0.1:7080"; // 请填写有度服务器地址
    private static final String APP_ID = "sysOrgAssistant"; // 请填写企业应用AppId
    private static final String APP_AESKEY = "8ZQRuCA4N7BJPLSWbzWD6744qP/axLwfs2kruYCJcMk="; // 请填写企业应用的EncodingaesKey

    private OrgDeptClient orgDeptClient;
    private int deptId = 0;

    public OrgDeptClientTest() throws Exception {
        YDApp app = new YDApp(BUIN, YDSERVER_HOST, "", APP_ID, "", APP_AESKEY);
        orgDeptClient = new OrgDeptClient(app);
    }

    //测试创建部门
    public void testCreateDept() throws ParamParserException, HttpRequestException, AESCryptoException {
        Dept dept = new Dept();
        dept.setName("test测试");
        dept.setParentId(0);
        dept.setAlias("abc");
        deptId = orgDeptClient.createDept(dept);
        System.out.println("create dept ok. deptId: "+deptId);
    }

    //测试修改部门
    public void testUpdateDept() throws ParamParserException, HttpRequestException, AESCryptoException {
        Dept dept = new Dept();
        dept.setId(deptId);
        dept.setName("test测试-1");
        dept.setAlias("abcd");
        orgDeptClient.updateDept(dept);
        System.out.println("update dept with id "+deptId+" ok.");
    }

    //测试获取部门
    public void testGetDept() throws ParamParserException, HttpRequestException, AESCryptoException {
        Dept dept = orgDeptClient.getDept(deptId);
        System.out.println("get dept with id "+deptId+" ok: "+dept);
    }

    //测试获取直属子部门列表
    public void testListDeptChildren() throws ParamParserException, HttpRequestException, AESCryptoException {
        List<Dept> depts = orgDeptClient.listDeptChildren(deptId);
        if(depts.size()==0){
            System.out.println("list dept children ok, found no children: "+deptId);
            return;
        }
        for(Dept dept : depts){
            System.out.println("get dept children: "+dept);
        }
    }

    //测试通过别名获取部门ID列表
    public void testGetDeptIdsByAlias() throws ParamParserException, HttpRequestException, AESCryptoException {
        String alias = "abcd";
        List<Dept> depts = orgDeptClient.listDeptIdByAlias(alias);
        if(depts.size()==0){
            System.out.println("no dept was found with alias: "+alias);
            return;
        }
        for(Dept dept : depts){
            System.out.println("list dept ids by alias "+alias+", i: "+dept.getId());
        }
    }

    //测试获取所有有别名的部门ID列表
    public void testGetAllDeptIdsHasAlias() throws ParamParserException, HttpRequestException, AESCryptoException {
        List<Dept> depts = orgDeptClient.listDeptIdByAlias("");
        if(depts.size()==0){
            System.out.println("no dept was found has alias");
            return;
        }
        for(Dept dept : depts){
            System.out.println("list all dept ids has alias, i: "+dept.getId()+", "+dept.getAlias());
        }
    }

    //测试删除部门
    public void testDeleteDept() throws ParamParserException, HttpRequestException, AESCryptoException {
        orgDeptClient.deleteDept(deptId);
        System.out.println("delete dept with id "+deptId+" ok.");
    }
}
