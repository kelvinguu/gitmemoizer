package com.github.memoize.git;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * User: Kelvin
 * Date: 12/4/13
 * Time: 9:46 PM
 */
public class GitIterableTests {

    private Repository repo;
    private File repoDir;
    private Set<String> correctFileSet;

    @Rule
    public ExternalResource resource = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            // construct repo files
            List<String> correctFiles = Arrays.asList("file1.txt", "sub/file2.txt");
            correctFileSet = new HashSet<String>(correctFiles);

            repoDir = Files.createTempDirectory("repoDir").toFile();

            for (String rp : correctFileSet) {
                File file = new File(repoDir, rp);
                File folder = file.getParentFile();
                folder.mkdirs();
                file.createNewFile();
            }

            repo = FileRepositoryBuilder.create(new File(repoDir, ".git"));
            repo.create();
            Git git = new Git(repo);
            git.add().addFilepattern(".").call();
            git.commit().setMessage("done").call();
        }

        @Override
        protected void after() {
            // destroy repo
            try {
                FileUtils.deleteDirectory(repoDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Test
    public void testCorrectFilesFound() throws IOException {
        ObjectId head = repo.resolve(Constants.HEAD);
        GitIterable gi = new GitIterable(repo, head, null);

        Set<String> testFileSet = new HashSet<String>();
        for (Object obj : gi) {
            GitFile gitFile = (GitFile) obj;
            testFileSet.add(gitFile.getGitFilePath());
        }

        assertEquals(correctFileSet, testFileSet);
    }
}
