package com.hundsun.ares.studio.ui.editor.text.sql;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;

/**
 * @author Hui Cao
 *  
 */
public class NestedMultiLineRule extends MultiLineRule
{
    protected int _commentNestingDepth = 0;

    /**
     * @param startSequence
     * @param endSequence
     * @param token
     */
    public NestedMultiLineRule(String startSequence, String endSequence, IToken token)
    {
        super(startSequence, endSequence, token);
    }

    /**
     * @param startSequence
     * @param endSequence
     * @param token
     * @param escapeCharacter
     */
    public NestedMultiLineRule(String startSequence, String endSequence, IToken token, char escapeCharacter)
    {
        super(startSequence, endSequence, token, escapeCharacter);
    }

    /**
     * @param startSequence
     * @param endSequence
     * @param token
     * @param escapeCharacter
     * @param breaksOnEOF
     */
    public NestedMultiLineRule(String startSequence, String endSequence, IToken token, char escapeCharacter,
        boolean breaksOnEOF)
    {
        super(startSequence, endSequence, token, escapeCharacter, breaksOnEOF);
    }

    protected boolean endSequenceDetected(ICharacterScanner scanner)
    {
        int c;
        char[][] delimiters = scanner.getLegalLineDelimiters();
        boolean previousWasEscapeCharacter = false;
        while ((c = scanner.read()) != ICharacterScanner.EOF)
        {
            if (c == fEscapeCharacter)
            {
                // Skip the escaped character.
                scanner.read();
            }
            else if (fStartSequence.length > 0 && c == fStartSequence[0])
            {
                // Check if the nested start sequence has been found.
                if (sequenceDetected(scanner, fStartSequence, false))
                {
                    _commentNestingDepth++;
                }
            }
            else if (fEndSequence.length > 0 && c == fEndSequence[0])
            {
                // Check if the specified end sequence has been found.
                if (sequenceDetected(scanner, fEndSequence, true))
                {
                    _commentNestingDepth--;
                }
                if (_commentNestingDepth <= 0)
                {
                    return true;
                }
            }
            previousWasEscapeCharacter = (c == fEscapeCharacter);
        }
        if (fBreaksOnEOF)
        {
            return true;
        }
        scanner.unread();
        return false;
    }

    protected IToken doEvaluate(ICharacterScanner scanner, boolean resume)
    {
        if (resume)
        {
            _commentNestingDepth = 0;
            if (scanner instanceof SQLPartitionScanner)
            {
                String scanned = ((SQLPartitionScanner) scanner).getScannedPartitionString();
                if (scanned != null && scanned.length() > 0)
                {
                    String startSequence = new String(fStartSequence);
                    int index = 0;
                    while ((index = scanned.indexOf(startSequence, index)) >= 0)
                    {
                        index++;
                        _commentNestingDepth++;
                    }
                    //must be aware of the closing sequences
                    String endSequence = new String(fEndSequence);
                    index = 0;
                    while ((index = scanned.indexOf(endSequence, index)) >= 0)
                    {
                        index++;
                        _commentNestingDepth--;
                    }
                }
            }
            if (endSequenceDetected(scanner))
            {
                return fToken;
            }

        }
        else
        {

            int c = scanner.read();
            if (c == fStartSequence[0])
            {
                if (sequenceDetected(scanner, fStartSequence, false))
                {
                    _commentNestingDepth = 1;
                    if (endSequenceDetected(scanner))
                    {
                        return fToken;
                    }
                }
            }
        }

        scanner.unread();
        return Token.UNDEFINED;

    }
}
