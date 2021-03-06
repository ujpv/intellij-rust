package org.rust.ide.actions

import com.intellij.openapi.actionSystem.IdeActions
import org.intellij.lang.annotations.Language
import org.rust.lang.RustTestCaseBase

class RustStringLiteralJoinLinesHandlerTest : RustTestCaseBase() {
    override val dataPath: String = ""

    private fun doTest(
        @Language("Rust") before: String,
        @Language("Rust") after: String
    ) {
        checkByText(before, after) {
            myFixture.performEditorAction(IdeActions.ACTION_EDITOR_JOIN_LINES)
        }
    }

    fun testNoEscape() = doTest(
        """
        fn main() {
            "Hello<caret>,
             World"
        }
        """,
        """
        fn main() {
            "Hello,<caret> World"
        }
        """
    )

    fun testNewlineEscape() = doTest(
        """
        fn main() {
            "He<caret>llo, \
             World"
        }
        """,
        """
        fn main() {
            "Hello,<caret> World"
        }
        """
    )

    fun testEscapedNewlineEscape() = doTest(
        """
        fn main() {
            "He<caret>llo, \\
             World"
        }
        """,
        """
        fn main() {
            "Hello, \\<caret> World"
        }
        """
    )

    fun testEscapedButNotEscapedInFactNewlineEscape() = doTest(
        """
        fn main() {
            "He<caret>llo, \\\
             World"
        }
        """,
        """
        fn main() {
            "Hello, \\<caret> World"
        }
        """
    )

    fun testTwoEscapedBackslashes() = doTest(
        """
        fn main() {
            "He<caret>llo, \\\\
             World"
        }
        """,
        """
        fn main() {
            "Hello, \\\\<caret> World"
        }
        """
    )

    fun testNoIndent() = doTest(
        """
        fn main() {
            "Hel<caret>lo,
World"
        }
        """,
        """
        fn main() {
            "Hello,<caret> World"
        }
        """
    )

    fun testOnlyNewlineEscape() = doTest(
        """
        fn main() {
            "<caret>\
            "
        }
        """,
        """
        fn main() {
            "<caret> "
        }
        """
    )
}
