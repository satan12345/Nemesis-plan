package com.able.zkstudy.com.able.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.springframework.util.Assert;

/**
 * @author jipeng
 * @date 2018-12-10 18:51
 * @description
 */
public class ZkCurator {

    private CuratorFramework curatorFramework;

    public ZkCurator(CuratorFramework curatorFramework) {
        Assert.notNull(curatorFramework,"参数不能为空");
        this.curatorFramework = curatorFramework;
    }
    public  void  init(){
        curatorFramework.usingNamespace("zk-curator-connector");
    }

    /**
     * 判断zk是否连接
     * @return
     */
    public boolean isAlived(){
        return curatorFramework.getState().equals(CuratorFrameworkState.STARTED);
    }
}

