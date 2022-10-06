package net.andreinc.neatchess.client.parser;

import net.andreinc.neatchess.client.model.BestMove;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class TestBestMoveParser {

    @Test
    void matchesWhenNoPonderSupplied() {
        final var bestMoveWithoutPonderLine = "bestmove a8c8";
        final var parser = new BestMoveParser();

        final var doMatch = parser.matches(bestMoveWithoutPonderLine);

        assertThat(doMatch, is(true));
    }

    @Test
    void matchesWhenPonderSupplied() {
        final var bestMoveWithPonderLine = "bestmove a8d5 ponder c8e6";
        final var parser = new BestMoveParser();

        final var doMatch = parser.matches(bestMoveWithPonderLine);

        assertThat(doMatch, is(true));
    }

    @Test
    void matchesWhenNoBEstMoveSupplied() {
        final var bestMoveWithPonderLine = "ponder c8e6";
        final var parser = new BestMoveParser();

        final var doMatch = parser.matches(bestMoveWithPonderLine);

        assertThat("Line without best move should not be matched as valid best move object.", doMatch, is(false));
    }

    @Test
    void doParseWithoutPonderSupplied() {
        final var bestMoveWithoutPonder = "bestmove a8c8";
        final var parser = new BestMoveParser();

        final var result = parser.parse(bestMoveWithoutPonder);
        final var expectedBestMove = new BestMove("a8c8", null);

        assertThat(result, is(expectedBestMove));
    }

    @Test
    void doParseWithPonderSupplied() {
        final var bestMoveWithoutPonder = "bestmove a8c8 ponder c8e6";
        final var parser = new BestMoveParser();

        final var result = parser.parse(bestMoveWithoutPonder);
        final var expectedBestMove = new BestMove("a8c8", "c8e6");

        assertThat(result, is(expectedBestMove));
    }
}