package romanow.abc.android;

public class TableStruct {
    private String name = "";
    private int style = 0;
    private int height;
    private int width;

    public TableStruct(String name, int style, int height, int width) {
        this.name = name;
        this.style = style;
        this.height = height;
        this.width = width;
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
}
