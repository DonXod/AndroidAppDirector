package romanow.abc.android;

public class TableStruct {
    private String name = "";
    private Long value = 0L;
    private int style = 0;
    private int height;
    private int width;
    private int indexName;
    private int graph = 0;
    private int dataSize = 0;
    private String labelY = "";

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public String getLabelY() {
        return labelY;
    }

    public void setLabelY(String labelY) {
        this.labelY = labelY;
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
