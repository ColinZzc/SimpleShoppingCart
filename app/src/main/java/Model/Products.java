package Model;

public class Products {
    private int id;
    private String image;
    private int num;
    private String title;
    private String price;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Products{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", num=" + num +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}