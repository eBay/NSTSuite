package com.ebay.nst.hosts.manager;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.Test;

public class PoolTypeTest {
	
    @Test
    public void testDevPoolType(){
        assertThat(PoolType.getType("dev"), is(equalTo(PoolType.DEV)));
        assertThat(PoolType.getType("dEv"),is(equalTo(PoolType.DEV)));
    }
}