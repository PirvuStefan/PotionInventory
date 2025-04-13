package whyarewesoclever.com.potionInventory;


public class PlayerItem {
    String json;
    String material;
    String display_name = null;
    String custom_name = null;
    PlayerItem( String material, String json){
        this.json = json;
        this.material = material;
    }
    public String getJson() {
        return json;
    }
    public String getMaterial() {
        return material;
    }
    public String getDisplayName() {
        return display_name;
    }
    public String getCustomName() {
        return custom_name;
    }

}
