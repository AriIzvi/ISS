import sys

path = "src/main/java/bookstore/BookStoreApp.java"
with open(path, "r", encoding="utf-8") as f:
    c = f.read()

c = c.replace("BG_YELLOW", "BG_BUTTER")
c = c.replace("TXT_EUCALYPTUS", "TXT_CHOCOLATE")
c = c.replace("BTN_PEACH", "MAIN_DUSTY_ROSE")
c = c.replace("HOVER_PEACH", "HOVER_ROSE")
c = c.replace("Color.WHITE", "BG_BUTTER_CARD")
c = c.replace("btnRemove.setForeground(BG_BUTTER_CARD);", "btnRemove.setForeground(Color.WHITE);")

old_consts = """    private static final Color BG_BUTTER = new Color(255, 250, 210); // Galben pai (Lămâie) – fundaluri
    private static final Color TXT_CHOCOLATE = new Color(46, 122, 92);    // Verde eucalipt – text și navigare
    private static final Color MAIN_DUSTY_ROSE = new Color(255, 170, 140); // Roz piersică – iconițe/detalii/secundar
    private static final Color HOVER_ROSE = new Color(255, 140, 110); // Roz piersică (Accent mai intens / Hover)"""

new_consts = """    private static final Color BG_BUTTER = new Color(253, 246, 227); // Crem de unt (Butter Cream)
    private static final Color BG_BUTTER_CARD = new Color(255, 253, 245); // Crem luminos pentru carduri
    private static final Color TXT_CHOCOLATE = new Color(82, 61, 56);    // Maro ciocolatiu stins
    private static final Color MAIN_DUSTY_ROSE = new Color(212, 163, 169); // Roz prăfuit (Dusty Rose)
    private static final Color HOVER_ROSE = new Color(196, 145, 151); // Accent roz prăfuit"""

c = c.replace(old_consts, new_consts)

c = c.replace("nav.setBackground(TXT_CHOCOLATE);", "nav.setBackground(MAIN_DUSTY_ROSE);")
c = c.replace("btn.setBackground(TXT_CHOCOLATE);", "btn.setBackground(MAIN_DUSTY_ROSE);")
c = c.replace("btn.setForeground(BG_BUTTER);", "btn.setForeground(TXT_CHOCOLATE);")
c = c.replace("btnBack.setBackground(TXT_CHOCOLATE);", "btnBack.setBackground(MAIN_DUSTY_ROSE);")
c = c.replace("btnBack.setForeground(BG_BUTTER);", "btnBack.setForeground(TXT_CHOCOLATE);")
c = c.replace("if (getBackground().equals(MAIN_DUSTY_ROSE) || getBackground().equals(TXT_CHOCOLATE))", "if (getBackground().equals(MAIN_DUSTY_ROSE))")

with open(path, "w", encoding="utf-8") as f:
    f.write(c)

print("Done")
