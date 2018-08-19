package io.github.wukn.util;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * DirCompareUtil: 文件夹对比工具类
 *
 * @author wukn
 * @date 2018-08-18
 */
public class DirCompareUtil {

    private File dirA;

    private File dirB;

    public DirCompareUtil(String pathA, String pathB) throws Exception {
        if (pathA == null || "".equals(pathA) || pathB == null || "".equals(pathB)) {
            throw new Exception("pathA and pathB cannot be null or empty string");
        }

        dirA = new File(pathA);
        dirB = new File(pathB);

        if (!dirA.exists() || !dirA.isDirectory() || !dirB.exists() || !dirB.isDirectory()) {
            throw new Exception("pathA and pathB must exist and is directory");
        }
    }

    /**
     * 对比两个文件夹，返回不同文件和文件夹的路径
     *
     * @return
     * @throws Exception
     */
    public List<String> compare() throws Exception {
        List<String> result = compareDirectory(dirA, dirB);
        return result;
    }


    /**
     * 递归对比两个文件夹，返回不同文件和文件夹的路径
     *
     * @param aDir
     * @param bDir
     * @return
     * @throws Exception
     */
    private List<String> compareDirectory(File aDir, File bDir) throws Exception {
        List<String> result = new ArrayList<>();

        List<File> aSubFiles = new ArrayList<>(Arrays.asList(aDir.listFiles()));
        List<File> bSubFiles = new ArrayList<>(Arrays.asList(bDir.listFiles()));

        // 遍历A文件夹下的内容
        for (File aFile : aSubFiles) {
            if (aFile.isFile()) {
                /**
                 * 如果在B中找到了同名文件，则判断文件内容是否一样；
                 *     如果一样，则无需处理；
                 *     如果不一样，则把两个文件都加到结果列表中；
                 *     把B中的这个同名文件从B的列表中移除；
                 * 如果B中没找到同名文件，则为A多出来的，加到结果列表中；
                 */
                Optional<File> bFileOptional = bSubFiles.stream()
                        .filter(f -> f.getName().equals(aFile.getName()) && f.isFile())
                        .findFirst();
                if (bFileOptional.isPresent()) {
                    File bFile = bFileOptional.get();

                    if (!isSameFile(aFile, bFile)) {
                        result.add(getRelativePath(dirA, aFile));
                        result.add(getRelativePath(dirB, bFile));
                    }

                    bSubFiles.remove(bFile);
                } else {
                    result.add(getRelativePath(dirA, aFile));
                }
            } else if (aFile.isDirectory()) {
                /**
                 * 如果在B中找到了同名文件夹，则递归判断两个文件夹内的差异；比较后将这个B的文件夹从B的列表中移除
                 * 如果在B中没有找到同名文件夹，则为A多出来的，加到结果列表中；
                 */
                Optional<File> bFileOptional = bSubFiles.stream()
                        .filter(f -> f.getName().equals(aFile.getName()) && f.isDirectory())
                        .findFirst();
                if (bFileOptional.isPresent()) {
                    File bFile = bFileOptional.get();
                    List<String> subResult = compareDirectory(aFile, bFile);

                    result.addAll(subResult);

                    bSubFiles.remove(bFile);
                } else {
                    result.add(getRelativePath(dirA, aFile));
                }
            }
        }

        // B文件夹内容列表中只剩下B多出来的
        for (File bFile : bSubFiles) {
            result.add(getRelativePath(dirB, bFile));
        }

        return result;
    }

    /**
     * 判断两个文件是否完全相同
     *
     * @param aFile
     * @param bFile
     * @return
     * @throws Exception
     */
    private boolean isSameFile(File aFile, File bFile) throws Exception {
        if (aFile == null || bFile == null) {
            return false;
        }

        if (!aFile.getName().equals(bFile.getName())) {
            return false;
        }

        if (aFile.length() != bFile.length()) {
            return false;
        }

        if (!Md5Util.getMd5ByFile(aFile).equals(Md5Util.getMd5ByFile(aFile))) {
            return false;
        }

        return true;
    }

    /**
     * 获取targetFile在baseFile下的相对路径，
     *
     * @param baseFile
     * @param targetFile
     * @return
     * @throws Exception
     */
    private String getRelativePath(File baseFile, File targetFile) throws Exception {
        String result = "";
        String baseDirPath = baseFile.getCanonicalFile().getParent() + File.separator;
        String targetFilePath = targetFile.getCanonicalPath();

        if (targetFilePath.startsWith(baseDirPath)) {
            result = targetFilePath.replaceFirst(baseDirPath, "");
        }

        return result;
    }


}
