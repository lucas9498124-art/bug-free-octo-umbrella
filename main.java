import java.util.*;

/**
 * Motor de regras em Java.
 * A versão HTML possui a interface visual; este Main.java demonstra
 * as mesmas regras centrais: dificuldade, economia, inventário,
 * equipamentos, pets, montarias, eventos e bosses.
 */
public class Main {
    static class Item {
        String id,nome,tipo,raridade; int preco,atk,def,mag;
        Item(String id,String nome,String tipo,String raridade,int preco,int atk,int def,int mag){
            this.id=id;this.nome=nome;this.tipo=tipo;this.raridade=raridade;
            this.preco=preco;this.atk=atk;this.def=def;this.mag=mag;
        }
    }
    static class Jogador {
        String nome="Herói",raca="Humano",classe="Guerreiro",mundo="Eldoria";
        int nivel=1,xp=0,proximoXP=100,ouro=100,vida=180,maxVida=180;
        int mana=80,maxMana=80,energia=60,maxEnergia=60,ataque=24,defesa=10,magia=18;
        int mortes=0,ng=0,ameaca=0,corrupcao=0;
        Set<String> inventario=new HashSet<>(),pets=new HashSet<>(),montarias=new HashSet<>();
        Set<String> bosses=new HashSet<>(),eventos=new HashSet<>();
        String petAtivo=null,montariaAtiva=null,arma=null,armadura=null;
    }
    static Map<String,Item> loja=new LinkedHashMap<>();
    static Map<String,Integer> bossHP=new LinkedHashMap<>();

    static {
        add("sword","Espada de Ferro","weapon","Comum",80,8,0,0);
        add("bow","Arco do Caçador","weapon","Incomum",450,18,0,0);
        add("dagger","Adaga Lunar","weapon","Raro",1400,32,0,0);
        add("abyss","Lâmina do Abismo","weapon","Épico",6500,65,0,10);
        add("dragon","Espada do Dragão","weapon","Lendário",22000,110,0,0);
        add("leather","Armadura de Couro","armor","Comum",120,0,7,0);
        add("steel","Armadura de Aço","armor","Incomum",700,0,20,0);
        add("frost","Armadura Glacial","armor","Raro",2200,0,42,0);
        add("astral","Couraça Astral","armor","Épico",9000,0,80,25);
        add("divine","Manto Divino","armor","Divino",50000,0,150,80);
        add("potion","Poção de Vida","consumable","Comum",60,0,0,0);
        add("mana","Poção de Mana","consumable","Comum",75,0,0,0);
        bossHP.put("Fenrir",850); bossHP.put("Ignaroth",2200);
        bossHP.put("Skolgrim",4800); bossHP.put("Nocturnus",9000);
        bossHP.put("Astrael",16000); bossHP.put("Nihilus",30000);
        bossHP.put("Julio",42000); bossHP.put("Gustavo",60000);
        bossHP.put("Lucas",1000000);
    }
    static void add(String id,String nome,String tipo,String raridade,int preco,int atk,int def,int mag){
        loja.put(id,new Item(id,nome,tipo,raridade,preco,atk,def,mag));
    }
    static double multiplicadorDificuldade(Jogador j){
        return 1+(j.nivel-1)*0.035+j.ng*0.25+j.ameaca*0.003;
    }
    static boolean comprar(Jogador j,String id){
        Item i=loja.get(id);
        if(i==null || j.ouro<i.preco)return false;
        j.ouro-=i.preco;j.inventario.add(id);return true;
    }
    static boolean equipar(Jogador j,String id){
        Item i=loja.get(id);
        if(i==null||!j.inventario.contains(id))return false;
        if(!i.tipo.equals("weapon")&&!i.tipo.equals("armor"))return false;
        if(i.tipo.equals("weapon"))j.arma=id;else j.armadura=id;
        return true;
    }
    static boolean eventoPodePagar(Jogador j,String chave){
        if(j.eventos.contains(chave))return false;
        j.eventos.add(chave);return true;
    }
    static int ataqueInimigo(Jogador j,int base,boolean noite){
        double m=multiplicadorDificuldade(j);
        if(noite)m*=1.20;
        int dano=Math.max(1,(int)(base*m-j.defesa*.22));
        j.vida-=dano;return dano;
    }
    static void derrotarBoss(Jogador j,String nome){
        if(!bossHP.containsKey(nome))return;
        j.bosses.add(nome);
        j.ouro+=Math.max(500,bossHP.get(nome)/10);
        if(nome.equals("Lucas"))j.corrupcao=100;
    }
    static void iniciarNGPlus(Jogador j){
        if(!j.bosses.contains("Lucas"))return;
        j.ng++;j.nivel=1;j.xp=0;j.proximoXP=100;j.ouro=500;
        j.maxVida=220;j.vida=220;j.maxMana=90;j.mana=90;
        j.maxEnergia=70;j.energia=70;j.ataque=28+j.ng*4;
        j.defesa=12+j.ng*2;j.magia=22+j.ng*3;
        j.bosses.clear();j.eventos.clear();j.mundo="Eldoria";
    }
    public static void main(String[] args){
        Jogador j=new Jogador();
        j.inventario.add("sword");
        System.out.println("=== CRÔNICAS DO ABISMO INFINITO ===");
        System.out.println("Dificuldade: ABISMO ABSOLUTO");
        System.out.println("Boss final: Lucas — 1.000.000 HP base");
        System.out.println("Itens possuídos: "+j.inventario.size());
        System.out.println("Loja: "+loja.size()+" itens cadastrados");
        System.out.println("Motor Java pronto.");
    }
}