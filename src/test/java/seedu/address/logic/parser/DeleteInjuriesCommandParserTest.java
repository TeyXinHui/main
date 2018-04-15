package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INJURIES_HISTORY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_INJURIES_HISTORY_DESC;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteInjuriesHistoryCommand;
import seedu.address.logic.commands.EditPersonDescriptor;
import seedu.address.testutil.EditPersonDescriptorBuilder;

//@@author chuakunhong
public class DeleteInjuriesCommandParserTest {


    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteInjuriesHistoryCommand.MESSAGE_USAGE);

    private DeleteInjuriesHistoryCommandParser parser = new DeleteInjuriesHistoryCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, INJURIES_HISTORY_DESC, MESSAGE_INVALID_FORMAT);

        // empty remark
        assertParseFailure(parser, "1" + INVALID_INJURIES_HISTORY_DESC, MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + INJURIES_HISTORY_DESC, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + INJURIES_HISTORY_DESC, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INJURIES_HISTORY_DESC;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withRemark(INJURIES_HISTORY_DESC).build();
        DeleteInjuriesHistoryCommand expectedCommand = new DeleteInjuriesHistoryCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
    //@@author
}
