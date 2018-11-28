//package net.ytoec.kernel.util;
//
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.FileSystemXmlApplicationContext;
//
//
//public class EncryptionDecryptionTest {
//    
//    @SuppressWarnings("unused")
//    @BeforeClass
//    public static void setUpBeforeClass() throws Exception {
//        ApplicationContext ctx = new FileSystemXmlApplicationContext(
//                "classpath*:applicationContext-common.xml");
//    }
//    
//    @Test
//    public void test(){
//        String test = "需要加密的字符串";
//        try{
//            EncryptionDecryption des = new EncryptionDecryption();// 自定义密钥
//            System.out.println("加密前的字符：" + test);
//            System.out.println("加密后的字符：" + des.encrypt(test));
//            System.out.println("解密后的字符：" + des.decrypt(des.encrypt(test)));
//    
//        }catch (Exception e) {
//            // TODO: handle exception
//            System.out.println("字符串加密异常："+e.getMessage());
//        }
//    }
//
// }
