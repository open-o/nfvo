package org.openo.nfvo.vimadapter.common;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openo.nfvo.vimadapter.common.VimOpResult.TaskStatus;

public class VimOpResultTest {
	@Test
	public void testVimOpResult(){
		TaskStatus operateStatus = TaskStatus.SUCCESS;
		String errorMessage = "error";
		VimOpResult vimOpResult = new VimOpResult(operateStatus, errorMessage);
		assertEquals("org.openo.nfvo.vimadapter.common.VimOpResult@[operateStatus=SUCCESS, errorCode=0, errorMessage=error", vimOpResult.toString());
		assertFalse(vimOpResult.isTimeout());
		assertFalse(vimOpResult.isTaskFailed());
		assertTrue(vimOpResult.isTaskSuccess());
		assertFalse(vimOpResult.isFinished());
	}
	
	@Test
	public void testIsFinished(){
		VimOpResult vimOpResult = new VimOpResult();
		assertTrue(vimOpResult.isFinished());
	}
	
	@Test
	public void testIsFinishedFail(){
		VimOpResult vimOpResult = new VimOpResult();
		vimOpResult.setOperateStatus(TaskStatus.FAIL);
		assertFalse(vimOpResult.isFinished());
		assertTrue(vimOpResult.isTaskFailed());
	}
	
	@Test
	public void testIsTimeout(){
		VimOpResult vimOpResult = new VimOpResult();
		vimOpResult.setOperateStatus(TaskStatus.TIMEOUT);
		assertTrue(vimOpResult.isTimeout());
	}
	
	@Test
	public void testIsTimeout1(){
		VimOpResult vimOpResult = new VimOpResult();
		vimOpResult.setOperateStatus(TaskStatus.PART_SUCCESS);
		assertFalse(vimOpResult.isTimeout());
	}
	
	@Test
	public void testIsTimeout2(){
		VimOpResult vimOpResult = new VimOpResult();
		vimOpResult.setOperateStatus(TaskStatus.RUNNING);
		assertFalse(vimOpResult.isTimeout());
	}

}
