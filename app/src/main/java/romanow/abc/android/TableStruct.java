package romanow.abc.android;

public class TableStruct {
    private String name = "";
    private int style = 0;
    private int height;
    private int width;
    private int graph = 0;

    public TableStruct(String name, int style, int height, int width, int graph) {
        this.name = name;
        this.style = style;
        this.height = height;
        this.width = width;
        this.graph = graph;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public TableStruct() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getGraph() {
        return graph;
    }

    public void setGraph(int graph) {
        this.graph = graph;
    }
}
