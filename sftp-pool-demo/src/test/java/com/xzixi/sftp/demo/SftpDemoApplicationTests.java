package com.xzixi.sftp.demo;

import com.xzixi.sftp.pool.Sftp;
import com.xzixi.sftp.pool.SftpPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SftpDemoApplicationTests {

    @Autowired
    private SftpPool sftpPool;

    @Test
    public void testPwd() throws Exception {
        Sftp sftp = sftpPool.borrowObject();
        String pwd = sftp.getChannelSftp().pwd();
        System.out.println(pwd); // 输出/root
        sftpPool.returnObject(sftp);
    }

}
