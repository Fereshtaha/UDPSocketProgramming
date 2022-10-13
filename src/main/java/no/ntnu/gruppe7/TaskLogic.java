package no.ntnu.gruppe7;

/**
 * Contains
 */
public class TaskLogic {
    public static final String TASK_REQUEST = "task";

    /**
     * Solve a task according to the assignment protocol
     *
     * @param task A task (sentence)
     * @return The answer according to the protocol: sentenceType wordCount
     */
    public static String solveTask(String task) {
        String type = sentenceType(task);
        if (type == null){
            return null;
        }

        return type + " " + wordCount(task);

    }

    /**
     * Count the number of words in the sentence
     *
     * @param sentence the sentence to check
     * @return Number of words; 0 if empty string or null given
     */
    static int wordCount(String sentence) {
        if (sentence == null || sentence.isEmpty() || sentence.length() == 1){
            return 0;
        }

        return sentence.split(" ").length;
    }

    /**
     * Detect the type of the sentence
     *
     * @param sentence A sentence, ending with . or ?
     * @return Statement or question
     */
    static String sentenceType(String sentence) {
        if (sentence == null){
            return null;
        }

        String type = "";
        if (sentence.endsWith("?")){
            type = "question";
        } else if (sentence.endsWith(".")){
            type = "statement";
        }

        return type;
    }

    public static boolean hasServerApproval(String response) {
        return !response.equals("error");
    }
}
