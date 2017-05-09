package game.io.input;

import world.GlobalCoordinate;

/**
 *
 */
class Selector<Selecting> {

    private final String prompt;
    private final GlobalCoordinate selectOrigin;
    private final int selectRange;
    private final SelectCallback<Selecting> callback;

    private boolean complete = false;


    public Selector(String prompt, GlobalCoordinate selectOrigin,
                    int selectRange, SelectCallback<Selecting> callback) {
        this.prompt = prompt;
        this.selectOrigin = selectOrigin;
        this.selectRange = selectRange;
        this.callback = callback;
    }


    void execute(Selecting selection) {
        if (complete) {
            throw new IllegalStateException("Attempted to execute callback for second time!");
        }
        callback.execute(selection);
        complete = true;
    }


    public String getPrompt() {
        return prompt;
    }

    public GlobalCoordinate getSelectOrigin() {
        return selectOrigin;
    }

    public int getSelectRange() {
        return selectRange;
    }

}
