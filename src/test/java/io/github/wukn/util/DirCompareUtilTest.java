package io.github.wukn.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class DirCompareUtilTest {

    @Test
    public void testCompare() throws Exception {

        /**
         * work on Linux
         */

        // given
        String aPath = "/tmp/dir_compare_test/目录A";
        String bPath = "/tmp/dir_compare_test/目录B";

        File testDir = new File("/tmp/dir_compare_test");
        if (testDir.exists()) {
            deleteFileOrDir(testDir);
        }
        testDir.mkdir();

        new File(aPath).mkdir();
        new File(bPath).mkdir();
        new File(aPath + "/文件A").createNewFile();
        new File(aPath + "/文件b").createNewFile();
        new File(aPath + "/目录a1").mkdir();
        new File(aPath + "/目录a1/文件a11").createNewFile();
        new File(aPath + "/目录a1/文件a12").createNewFile();
        new File(aPath + "/目录a2").mkdir();
        new File(aPath + "/目录a2/文件a21").createNewFile();
        new File(aPath + "/目录a2/文件a22").createNewFile();

        new File(bPath + "/文件A").createNewFile();
        new File(bPath + "/文件b1").createNewFile();
        new File(bPath + "/目录a1").mkdir();
        new File(bPath + "/目录a1/文件a11").createNewFile();
        new File(bPath + "/目录a1/文件a12").createNewFile();
        new File(bPath + "/目录a1/文件3").createNewFile();

        // when
        DirCompareUtil dirCompareUtil = new DirCompareUtil(aPath, bPath);
        List<String> result = dirCompareUtil.compare();

        // then
        Assert.assertEquals(4, result.size());
        Assert.assertTrue("error", result.contains("目录A/文件b"));
        Assert.assertTrue("error", result.contains("目录B/文件b1"));
        Assert.assertTrue("error", result.contains("目录A/目录a2"));
        Assert.assertTrue("error", result.contains("目录B/目录a1/文件3"));

    }

    private void deleteFileOrDir(File path) {
        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteFileOrDir(files[i]);
        }
        path.delete();
    }
}
