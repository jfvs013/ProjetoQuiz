package backend.dao;

/**
 * TAD para as perguntas do quiz.
 *
 * @author leonardo
 */
public class DBQuiz {

    private final int id;
    private final String question;
    private final int category;
    private final boolean answer;
    private final char level;

    public DBQuiz(int id, String question, int category, boolean answer, char level) {
        this.id = id;
        this.question = question;
        this.category = category;
        this.answer = answer;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public int getCategory() {
        return category;
    }

    public boolean isAnswer() {
        return answer;
    }

    public char getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "DBQuiz{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", category=" + category +
                ", answer=" + answer +
                ", level=" + level +
                '}';
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DBQuiz other = (DBQuiz) obj;
        return this.id == other.id;
    }
}