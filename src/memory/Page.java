package memory;

public class Page {
    private int typStrony;
    public int bitOdwolania = 1;

    public Page(int typStrony) {
        this.typStrony = typStrony;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return typStrony == page.typStrony;
    }
}
