package marvin.liu.fileloader;

import com.google.common.collect.Lists;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.easybatch.core.job.DefaultJobReportMerger;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobReportMerger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A tool to download and store multiple files at same time from URLs specified in a given input txt file.
 *
 * @author marvin.liu.
 */
public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    private AppConfig appConfig;

    private URLDownloader urlDownloader;

    public App(URLDownloader urlDownloader, Config config) {
        this.urlDownloader = urlDownloader;
        this.appConfig = new AppConfig(config);
    }

    /**
     * Load config and delegate job to URLDownloader
     *
     * @param source path of source file of URLs
     * @param target path of directory where downloaded files to be stored
     * @throws URLDownloaderException when errors happen in URLDownloader
     */
    public void run(String source, String target) throws URLDownloaderException {
        ExecutorService executorService = Executors.newFixedThreadPool(appConfig.numberOfDownloaderThreads());

        List<Future<JobReport>> partialReports;
        List<JobReport> reports = Lists.newArrayList();

        try {
            partialReports = executorService.invokeAll(jobList);

            for ( Future<JobReport> jobReportFuture : partialReports ) {
                reports.add(jobReportFuture.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new URLDownloaderException("Batch Download or Report Job failed", e);
        }

        JobReportMerger reportMerger = new DefaultJobReportMerger();
        JobReport report = reportMerger.mergerReports(reports.stream().toArray(JobReport[]::new));

        executorService.shutdown();
    }

    public static void main( String[] args ) throws Exception {
        //Read source file and target dir from args
        ArgumentParser parser = ArgumentParsers.newArgumentParser("App")
                .description("Download files from URLs specified in a given input txt file");

        parser.addArgument("-s", "--source")
                .type(String.class)
                .required(true)
                .help("Specify location of input txt file");

        parser.addArgument("-t", "--target")
                .type(String.class)
                .required(true)
                .help("Specify location of download files");

        try {
            Namespace ns = parser.parseArgs(args);

            String source = ns.getString("source");
            String target = ns.getString("target");

            LOG.debug("source file: {}, target dir: {}", source, target);

            Config config = ConfigFactory.load();

            URLDownloader easyBatchURLDownloader = new DefaultURLDownloader();

            App app = new App(easyBatchURLDownloader, config);
            app.run(source, target);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
    }
}