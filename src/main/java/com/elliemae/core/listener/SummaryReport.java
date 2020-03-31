package com.elliemae.core.listener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestNG;
import org.testng.collections.Lists;
import org.testng.xml.XmlSuite;

import com.elliemae.core.Logger.EllieMaeLog;
import com.elliemae.core.Logger.EllieMaeLog.EllieMaeLogLevel;
import com.elliemae.core.Utils.CommonUtility;
import com.elliemae.core.Utils.CommonUtility.FileType;

/* Class to customize TestNG's default emailable report. 
 * It creates "summary-report.html" customized TestNG report file. 
 * It uses actual test execution count and also prints it on console. */
public class SummaryReport implements IReporter 
{

    private static final Logger _log = Logger.getLogger(SummaryReport.class);
    private PrintWriter out;
    private int row;
    private Integer testIndex;
    private int methodIndex;
    
    String date;
    String folderNameWithTimeStamp = CommonUtility.currentTimeStamp;
    String destinationPath = System.getProperty("user.dir") + File.separator + "AutomationOutput";
    

    /** Override generateReport method */
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
                               String outdir) {
        try {
            out = createWriter(outdir);
        } catch (IOException e) {
            _log.error("output file", e);
            return;
        }

        startHtml(out);
        generateSuiteSummaryReport(suites);
        generateMethodSummaryReport(suites);
        endHtml(out);
        out.flush();
        out.close();
        
    	TestNG.getDefault().setXmlSuites(xmlSuites);
        String sourceFilePath = outdir + File.separator + "summary-report.html";
        CommonUtility.copyFilesOrFolder(sourceFilePath, destinationPath + File.separator + "summary-report.html",FileType.FILE);
    }

    protected PrintWriter createWriter(String outdir) throws IOException 
    {

        new File(outdir).mkdirs();
        return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "summary-report.html"))));

    }

    /**
     * Creates a table showing the highlights of each test method with links to
     * the method details
     */
    protected void generateMethodSummaryReport(List<ISuite> suites) {
        methodIndex = 0;
        startResultSummaryTable("methodOverview");
        int testIndex = 1;
        for (ISuite suite : suites) {
            if (suites.size() >= 1) {
                titleRow(suite.getName(), 4);
            }
            Map<String, ISuiteResult> r = suite.getResults();
            for (ISuiteResult r2 : r.values()) {
                ITestContext testContext = r2.getTestContext();
                String testName = testContext.getName();
                this.testIndex = testIndex;
                resultSummary(suite, testContext.getFailedConfigurations(),
                        testName, "failed", " (configuration methods)");
                resultSummary(suite, testContext.getFailedTests(), testName,
                        "failed", "");
                resultSummary(suite, testContext.getSkippedConfigurations(),
                        testName, "skipped", " (configuration methods)");
                resultSummary(suite, testContext.getSkippedTests(), testName,
                        "skipped", "");
                resultSummary(suite, testContext.getPassedTests(), testName,
                        "passed", "");
                testIndex++;
            }
        }
        out.println("</table>");
    }

    /**
     * @param tests
     */
    private void resultSummary(ISuite suite, IResultMap tests, String testname,
                               String style, String details) {
        if (tests.getAllResults().size() > 0) {
            StringBuffer buff = new StringBuffer();
            String lastClassName = "";
            int mq = 0;
            int cq = 0;
            for (ITestNGMethod method : getMethodSet(tests, suite)) {
                row += 1;
                methodIndex += 1;
                ITestClass testClass = method.getTestClass();
                String className = testClass.getName();
                if (mq == 0) {
                    String id = (testIndex == null ? null : "t"
                            + Integer.toString(testIndex));
                    titleRow(testname + " &#8212; " + style + details, 5, id);
                    testIndex = null;
                }
                if (!className.equalsIgnoreCase(lastClassName)) {
                    if (mq > 0) {
                        cq += 1;
                        out.print("<tr class=\"" + style
                                + (cq % 2 == 0 ? "even" : "odd") + "\">"
                                + "<td");
                        if (mq > 1) {
                            out.print(" rowspan=\"" + mq + "\"");
                        }
                        out.println(">" + lastClassName + "</td>" + buff);
                    }
                    mq = 0;
                    buff.setLength(0);
                    lastClassName = className;
                }
                Set<ITestResult> resultSet = tests.getResults(method);
                long end = Long.MIN_VALUE;
                long start = Long.MAX_VALUE;
                long startMS=0;
                for (ITestResult testResult : tests.getResults(method)) {
                    if (testResult.getEndMillis() > end) {
                        end = testResult.getEndMillis()/1000;
                    }
                    if (testResult.getStartMillis() < start) {
                        startMS = testResult.getStartMillis();
                        start =startMS/1000;
                    }
                }

                DateFormat formatter = new SimpleDateFormat("kk:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startMS);

                mq += 1;
                if (mq > 1) {
                    buff.append("<tr class=\"" + style
                            + (cq % 2 == 0 ? "odd" : "even") + "\">");
                }
                String description = method.getDescription();
                String testInstanceName = resultSet
                        .toArray(new ITestResult[] {})[0].getTestName();
                buff.append("<td><a href=\"#m"
                        + methodIndex
                        + "\">"
                        + qualifiedName(method)
                        + " "
                        + (description != null && description.length() > 0 ? "(\""
                        + description + "\")"
                        : "")
                        + "</a>"
                        + (null == testInstanceName ? "" : "<br>("
                        + testInstanceName + ")") + "</td>"
//                        + "<td class=\"numi\" style=\"text-align:left;padding-right:2em\">" + firstLine+"<br/>"+screenshotLnk+ "</td>"
                        + "<td style=\"text-align:right\">" + formatter.format(calendar.getTime()) + "</td>" + "<td class=\"numi\">"
                        + millisToTimeConversion(end - start) + "</td>" + "</tr>");
            }
            if (mq > 0) {
                cq += 1;
                out.print("<tr class=\"" + style
                        + (cq % 2 == 0 ? "even" : "odd") + "\">" + "<td");
                if (mq > 1) {
                    out.print(" rowspan=\"" + mq + "\"");
                }
                out.println(">" + lastClassName + "</td>" + buff);
            }
        }
    }

    private String millisToTimeConversion(long seconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int minutes = (int) (seconds / SECONDS_IN_A_MINUTE);
        seconds -= minutes * SECONDS_IN_A_MINUTE;

        int hours = minutes / MINUTES_IN_AN_HOUR;
        minutes -= hours * MINUTES_IN_AN_HOUR;

        return prefixZeroToDigit(hours) + ":" + prefixZeroToDigit(minutes) + ":" + prefixZeroToDigit((int)seconds);
    }

    private String prefixZeroToDigit(int num){
        int number=num;
        if(number<=9){
            String sNumber="0"+number;
            return sNumber;
        }
        else
            return ""+number;

    }

    /** Starts and defines columns result summary table */
    private void startResultSummaryTable(String style) {
        tableStart(style, "summary");
        out.println("<tr><th>Class</th>"
                + "<th>Method</th><th>Start Time </th><th>Time<br/>(hh:mm:ss)</th></tr>");
        row = 0;
    }

    private String qualifiedName(ITestNGMethod method) {
        StringBuilder addon = new StringBuilder();
        String[] groups = method.getGroups();
        int length = groups.length;
        if (length > 0 && !"basic".equalsIgnoreCase(groups[0])) {
            addon.append("(");
            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    addon.append(", ");
                }
                addon.append(groups[i]);
            }
            addon.append(")");
        }

        return "<b>" + method.getMethodName() + "</b> " + addon;
    }

//    protected void generateExceptionReport(Throwable exception,
//                                           ITestNGMethod method) {
//        out.print("<div class=\"stacktrace\">");
//        out.print(Utils.stackTrace(exception, true)[0]);
//        out.println("</div>");
//    }

    /**
     * Since the methods will be sorted chronologically, we want to return the
     * ITestNGMethod from the invoked methods.
     */
    private Collection<ITestNGMethod> getMethodSet(IResultMap tests,
                                                   ISuite suite) {
        List<IInvokedMethod> r = Lists.newArrayList();
        List<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();
        for (IInvokedMethod im : invokedMethods) {
            //System.out.println("suite.getAllInvokedMethods()  .."+im);
            if (tests.getAllMethods().contains(im.getTestMethod())) {
                r.add(im);
            }
        }
        //System.out.println("r ....."+ r.toString());
        //Arrays.sort(r.toArray(new IInvokedMethod[r.size()]), new TestSorter());
        Collections.sort(r,new TestSorter());

        //System.out.println("Sorted Array .."+r.toString());
        List<ITestNGMethod> result = Lists.newArrayList();

        // Add all the invoked methods
        for (IInvokedMethod m : r) {
            for(ITestNGMethod temp:result){
                if(!temp.equals(m.getTestMethod()))
                    result.add(m.getTestMethod());
            }
        }

        // Add all the methods that weren't invoked (e.g. skipped) that we
        // haven't added yet

        Collection<ITestNGMethod> allMethodsCollection=tests.getAllMethods();
        List<ITestNGMethod> allMethods=new ArrayList<ITestNGMethod>(allMethodsCollection);
        //System.out.println("All methods before sort"+ allMethods.toString());
        Collections.sort(allMethods, new TestMethodSorter());
        //System.out.println("After sorting "+allMethods.toString());

        //for (ITestNGMethod m : tests.getAllMethods()) {
        for (ITestNGMethod m : allMethods) {
            //System.out.println("tests.getAllMethods()  .."+m);
            if (!result.contains(m)) {
                result.add(m);
            }
        }
        //System.out.println("results ....."+ result.toString());
        return result;
    }

    @SuppressWarnings("unused")
    public void generateSuiteSummaryReport(List<ISuite> suites) {
        tableStart("testOverview", null);
        out.print("<tr>");
        tableColumnStart("Test");
        tableColumnStart("# Passed");
        tableColumnStart("# Skipped");
        tableColumnStart("# Failed");
        tableColumnStart("Start<br/>Time");
        tableColumnStart("End<br/>Time");
        tableColumnStart("Total<br/>Time(hh:mm:ss)");
        out.println("</tr>");
        NumberFormat formatter = new DecimalFormat("#,##0.0");
        int qty_tests = 0;
        int qty_pass_m = 0;
        int qty_pass_s = 0;
        int qty_skip = 0;
        int qty_fail = 0;
        long time_start = Long.MAX_VALUE;
        long time_end = Long.MIN_VALUE;
        testIndex = 1;
        for (ISuite suite : suites) {
            if (suites.size() >= 1) {
                titleRow(suite.getName(), 10);
            }
            Map<String, ISuiteResult> tests = suite.getResults();
            for (ISuiteResult r : tests.values()) {
                qty_tests += 1;
                ITestContext overview = r.getTestContext();
                
                
                int TOTAL_PASS_COUNT =0 ;
                int TOTAL_FAIL_COUNT =0;
                
                // if TOTAL_TEST_COUNT is 0 then use TestNG default test count
                if ((int)overview.getAttribute("TOTAL_TEST_COUNT")==0)
                {
            		TOTAL_PASS_COUNT = getMethodSet(overview.getPassedTests(), suite).size();
            		TOTAL_FAIL_COUNT = getMethodSet(overview.getFailedTests(), suite).size();
                }
                else // else use customized Test count from TestStatistics
                {
	        		TOTAL_PASS_COUNT = (int) overview.getAttribute("TOTAL_PASS_COUNT");
	        		TOTAL_FAIL_COUNT = (int) overview.getAttribute("TOTAL_FAIL_COUNT");
                }
                
                startSummaryRow(overview.getName());
                int q = TOTAL_PASS_COUNT;
                qty_pass_m += q;
                summaryCell(q, Integer.MAX_VALUE);
                q = getMethodSet(overview.getSkippedTests(), suite).size();
                qty_skip += q;
                summaryCell(q, 0);
                q = TOTAL_FAIL_COUNT;
                qty_fail += q;
                summaryCell(q, 0);

                SimpleDateFormat summaryFormat = new SimpleDateFormat("kk:mm:ss");
                summaryCell(summaryFormat.format(overview.getStartDate()),true);
                out.println("</td>");

                summaryCell(summaryFormat.format(overview.getEndDate()),true);
                out.println("</td>");

                time_start = Math.min(overview.getStartDate().getTime(),
                        time_start);
                time_end = Math.max(overview.getEndDate().getTime(), time_end);
                summaryCell(millisToTimeConversion((overview.getEndDate().getTime() - overview
                        .getStartDate().getTime()) / 1000), true);

                out.println("</tr>");
                testIndex++;
            }
        }
        if (qty_tests > 1) {
            out.println("<tr class=\"total\"><td>Total</td>");	
            summaryCell(qty_pass_m, Integer.MAX_VALUE);
            summaryCell(qty_skip, 0);
            summaryCell(qty_fail, 0);
            summaryCell(" ", true);
            summaryCell(" ", true);
            summaryCell(millisToTimeConversion(((time_end - time_start) / 1000)), true);
            //out.println("<td colspan=\"3\">&nbsp;</td></tr>");
            out.println("</tr>");
        }
        out.println("</table>");
        int totalExecutedTestCases = qty_pass_m+qty_fail;
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "*******************************************************************************************", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "Total Tests Executed: "+totalExecutedTestCases+", PASS: "+qty_pass_m+", FAIL: "+qty_fail+"", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "*******************************************************************************************", EllieMaeLogLevel.reporter);
		EllieMaeLog.log(_log, "\n", EllieMaeLogLevel.reporter);
    }

    private void summaryCell(String v, boolean isgood) {
        out.print("<td class=\"numi" + (isgood ? "" : "_attn") + "\">" + v
                + "</td>");
    }

    private void startSummaryRow(String label) {
        row += 1;
        out.print("<tr"
                + (row % 2 == 0 ? " class=\"stripe\"" : "")
                + "><td style=\"text-align:left;padding-right:2em\"><a href=\"#t"
                + testIndex + "\"><b>" + label + "</b></a>" + "</td>");

    }

    private void summaryCell(int v, int maxexpected) {
        summaryCell(String.valueOf(v), v <= maxexpected);
    }

    private void tableStart(String cssclass, String id) {
        out.println("<table cellspacing=\"0\" cellpadding=\"0\""
                + (cssclass != null ? " class=\"" + cssclass + "\""
                : " style=\"padding-bottom:2em\"")
                + (id != null ? " id=\"" + id + "\"" : "") + ">");
        row = 0;
    }

    private void tableColumnStart(String label) {
        out.print("<th>" + label + "</th>");
    }

    private void titleRow(String label, int cq) {
        titleRow(label, cq, null);
    }

    private void titleRow(String label, int cq, String id) {
        out.print("<tr");
        if (id != null) {
            out.print(" id=\"" + id + "\"");
        }
        out.println("><th colspan=\"" + cq + "\">" + label + "</th></tr>");
        row = 0;
    }

    /** Starts HTML stream */
    protected void startHtml(PrintWriter out) {
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        out.println("<head>");
        out.println("<title>TestNG Report</title>");
        out.println("<style type=\"text/css\">");
        out.println("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show}");
        out.println("td,th {border:1px solid #009;padding:.25em .5em}");
        out.println(".result th {vertical-align:bottom}");
        out.println(".param th {padding-left:1em;padding-right:1em}");
        out.println(".param td {padding-left:.5em;padding-right:2em}");
        out.println(".stripe td,.stripe th {background-color: #E6EBF9}");
        out.println(".numi,.numi_attn {text-align:right}");
        out.println(".total td {font-weight:bold}");
        out.println(".passedodd td {background-color: #0A0}");
        out.println(".passedeven td {background-color: #3F3}");
        out.println(".skippedodd td {background-color: #CCC}");
        out.println(".skippedodd td {background-color: #DDD}");
        out.println(".failedodd td,.numi_attn {background-color: #F33}");
        out.println(".failedeven td,.stripe .numi_attn {background-color: #D00}");
        out.println(".stacktrace {white-space:pre;font-family:monospace}");
        out.println(".totop {font-size:85%;text-align:center;border-bottom:2px solid #000}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
    }

    /** Finishes HTML stream */
    protected void endHtml(PrintWriter out) {
        out.println("</body></html>");
    }

    // ~ Inner Classes --------------------------------------------------------
    /** Arranges methods by classname and method name */
    private class TestSorter implements Comparator<IInvokedMethod> {
        // ~ Methods
        // -------------------------------------------------------------

        @SuppressWarnings("unchecked")
		/** Arranges methods by classname and method name */
        @Override
        public int compare(IInvokedMethod o1, IInvokedMethod o2) {
            int r =o1.getTestMethod().getTestClass().getName().compareTo(o2.getTestMethod().getTestClass().getName());
            if (r == 0) {
                r=o1.getTestMethod().compareTo(o2.getTestMethod());

            }
            return r;
        }

    }

    private class TestMethodSorter implements Comparator<ITestNGMethod> {
        @Override
        public int compare(ITestNGMethod o1, ITestNGMethod o2) {
            int r =o1.getTestClass().getName().compareTo(o2.getTestClass().getName());
            if (r == 0) {
                r=o1.getMethodName().compareTo(o2.getMethodName());
            }
            return r;
        }
    }

}
