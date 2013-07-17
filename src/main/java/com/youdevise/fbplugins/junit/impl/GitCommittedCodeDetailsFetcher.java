package com.youdevise.fbplugins.junit.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.joda.time.DateTime;

import com.youdevise.fbplugins.junit.CommittedCodeDetailsFetcher;
import com.youdevise.fbplugins.junit.LineOfCommittedCode;

public class GitCommittedCodeDetailsFetcher implements CommittedCodeDetailsFetcher {

    private final Repository gitRepo;

    public GitCommittedCodeDetailsFetcher(Repository gitRepo) {
        this.gitRepo = gitRepo;
    }

    @Override
    public List<LineOfCommittedCode> logHistoryOfFile(String relativePathToGitControlledFile) {
        List<LineOfCommittedCode> linesOfCode = new ArrayList<LineOfCommittedCode>();
        try {
            BlameResult blameResult = new BlameCommand(gitRepo).setFilePath(relativePathToGitControlledFile).call();

            for (int lineIndex = 0; ; lineIndex++) {
                RevCommit sourceCommit = blameResult.getSourceCommit(lineIndex);
                if (sourceCommit != null) {
                    linesOfCode.add(lineOfCommitedCodeFor(blameResult, sourceCommit, lineIndex));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            ; // BlameResult does not seem to expose the number of available indexes, so we have to use this to end the for loop :-(
        } catch (GitAPIException gite) {
            System.out.println(gite.getMessage());
            return Collections.emptyList();
        }
        return linesOfCode;
    }

    private LineOfCommittedCode lineOfCommitedCodeFor(BlameResult blameResult, RevCommit sourceCommit, int lineIndex) {
        int commitTimeInSecondsSinceEpoch = sourceCommit.getCommitTime();
        DateTime dateOfCommit = new DateTime(1000L * commitTimeInSecondsSinceEpoch);
        String revision = sourceCommit.getId().name();
        String author = blameResult.getSourceAuthor(lineIndex).getName();
        String lineContents = blameResult.getResultContents().getString(lineIndex);
        int lineNumber = blameResult.getSourceLine(lineIndex);
        
        return new LineOfCommittedCode(dateOfCommit, revision, author, lineContents, lineNumber);
    }
}
