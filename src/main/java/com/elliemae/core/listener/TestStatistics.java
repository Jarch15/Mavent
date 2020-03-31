package com.elliemae.core.listener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;

import com.elliemae.core.annotation.ApplyRetryListener;

/* TestStatistics Listener class to count actual number test cases executed, passed and failed.
 * This count to be printed on console and to be added in customized TestNG summary report */
public class TestStatistics extends PostTestListener 
{

	public static Logger _log = Logger.getLogger(TestStatistics.class);
	
	/* (non-Javadoc)
	 * @see org.testng.ITestListener#onStart(org.testng.ITestContext)
	 */
	@Override
	public void onStart(ITestContext context)
	{
		Map<String, String> testStatusMap =  new HashMap<String, String> ();
		context.setAttribute("testStatusMap",testStatusMap);
	}
	
	/**
	 * <b>Name:</b> onFinish
	 * <b>Description:</b> Method to perform cleanup steps post finish operation of test workflow cycle
	 * 
	 * @see org.testng.ITestListener#onFinish(org.testng.ITestContext)
	 */
	@Override
	public void onFinish(ITestContext context) {
		
		Set<ITestResult> passedTestsResult, failedTestsResult, skippedTestsResult, currTestsResult2;
		List<ITestResult> redundantEntries = new LinkedList<>();
		
		/** Clean up the passed, failed and skipped test results of redundant entries for retried method(s). */
		// Passed test cleanup.
		passedTestsResult = context.getPassedTests().getAllResults();
		
		for(ITestResult currPassedTestResult: passedTestsResult) {
			if(null != currPassedTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(ApplyRetryListener.class)) {
				if(0 < context.getFailedTests().getResults(currPassedTestResult.getMethod()).size()) {
					failedTestsResult = context.getFailedTests().getAllResults();
					
					for(ITestResult currFailedTestResult: failedTestsResult) {
						if(currPassedTestResult.getMethod().getConstructorOrMethod().getName().equals(currFailedTestResult.getMethod().getMethodName()) && isEqual(currPassedTestResult.getParameters(), currFailedTestResult.getParameters())) {
							failedTestsResult.remove(currFailedTestResult);
						}
					}
				}
				
				if(0 < context.getSkippedTests().getResults(currPassedTestResult.getMethod()).size()) {
					skippedTestsResult = context.getSkippedTests().getAllResults();

					for(ITestResult currSkippedTestResult: skippedTestsResult) {
						if(currPassedTestResult.getMethod().getConstructorOrMethod().getName().equals(currSkippedTestResult.getMethod().getMethodName()) && isEqual(currPassedTestResult.getParameters(), currSkippedTestResult.getParameters())) {		
							skippedTestsResult.remove(currSkippedTestResult);
						}
					}
				}
			}
		}
		
		// Failed test cleanup
		failedTestsResult = context.getFailedTests().getAllResults();
		
		for(ITestResult currFailedTestResult: failedTestsResult) {
			if(null != currFailedTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(ApplyRetryListener.class)) {
				skippedTestsResult = context.getSkippedTests().getAllResults();

				if(0 < context.getSkippedTests().getResults(currFailedTestResult.getMethod()).size()) {
					for(ITestResult currSkippedTestResult: skippedTestsResult) {
						if(currFailedTestResult.getMethod().getConstructorOrMethod().getName().equals(currSkippedTestResult.getMethod().getMethodName()) && isEqual(currFailedTestResult.getParameters(), currSkippedTestResult.getParameters())) {
							skippedTestsResult.remove(currSkippedTestResult);
						}
					}
				}
			}
		}
		
		// Skipped test cleanup
		skippedTestsResult = context.getSkippedTests().getAllResults();
		
		for(ITestResult currSkippedTestResult: skippedTestsResult) {
			if(null != currSkippedTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(ApplyRetryListener.class)) {
				currTestsResult2 = context.getFailedTests().getResults(currSkippedTestResult.getMethod());
				
				if(null != currTestsResult2 && 1 < currTestsResult2.size()) {
					for(ITestResult currSkippedTestResult2: currTestsResult2) {
						if(currSkippedTestResult.getMethod().getConstructorOrMethod().getName().equals(currSkippedTestResult2.getMethod().getMethodName()) && isEqual(currSkippedTestResult.getParameters(), currSkippedTestResult2.getParameters())) {
							if(currSkippedTestResult.equals(currSkippedTestResult2)) {
								continue;
							}
							else if(currSkippedTestResult.getStartMillis() < currSkippedTestResult2.getStartMillis()) {
								redundantEntries.add(currSkippedTestResult2);
							}
							else if(currSkippedTestResult.getStartMillis() > currSkippedTestResult2.getStartMillis()) {
								redundantEntries.add(currSkippedTestResult);
							}
						}
					}
				}
			}
		}
		
		if(null != redundantEntries && !redundantEntries.isEmpty()) {
			skippedTestsResult.removeAll(redundantEntries);
			redundantEntries.clear();
		}
		
		// Actual Test Case Count
		@SuppressWarnings("unchecked")
		Map<String, String> testStatusMap =  (Map<String, String>) context.getAttribute("testStatusMap");
		int totalTestCount = 0;
		int totalPassCount = 0;
		int totalFailCount = 0;
		
		for(Entry<String,String> result : testStatusMap.entrySet())
		{
			totalTestCount++;
			if(result.getValue().equalsIgnoreCase("Success") || result.getValue().equalsIgnoreCase("PASS"))
			{
				totalPassCount++;
			}
			else
			{
				totalFailCount++;
			}
			
		}
		
		context.setAttribute("TOTAL_TEST_COUNT", totalTestCount);
		context.setAttribute("TOTAL_PASS_COUNT", totalPassCount);
		context.setAttribute("TOTAL_FAIL_COUNT", totalFailCount);
		
	}

}