package romanow.abc.android;

public class TableStruct {
    private String name = "";
    private Long value = 0L;
    private int style = 0;
    private int height;
    private int width;
    private int indexName;
    private int graph = 0;

    public TableStruct(String name, int style, int height, int width, int graph, Long value, int indexName) {
        this.name = name;
        this.style = style;
        this.height = height;
        this.width = width;
        this.graph = graph;
        this.value = value;
        this.indexName = indexName;
    }

    public int getIndexName() {
        return indexName;
    }

    public void setIndexName(int indexName) {
        this.indexName = indexName;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
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
