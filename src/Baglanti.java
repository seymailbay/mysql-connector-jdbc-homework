import com.mysql.cj.protocol.Resultset;

import java.sql.*;
import java.util.Scanner;

public class Baglanti {
    private String kullaniciAdi="root";
    private String parola="";

    private String dbİsmi="demo";

    private String host = "localhost"; //hostumuzu tanımladık
    private int port=3306;
    private Connection con = null;
    private Statement statement=null; //STATEMENT SQL SORGULARI İÇİN

    private PreparedStatement preparedStatement=null;

    public void IDBuyukUcSil(){

        try {
            statement=con.createStatement();
            String sorgu="Delete from calisanlar where id<3";
            statement.executeUpdate(sorgu);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void calisanGuncelle(){

        try {
            statement=con.createStatement();

            String sorgu="Update calisanlar Set email= 'Mustafamuratcoskun@gmail.com' where id=1";
            statement.executeUpdate(sorgu);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void calisanlariGetir(){

        String sorgu="Select * from calisanlar ";

        try {
            statement=con.createStatement();
           ResultSet rs=statement.executeQuery(sorgu);//veritabanında guncelleme yapmıyorsak kullanılır.icine aldıgı sql sorgusunu calıstırır.
           while(rs.next()){ //daha okucamız veri varmı ? iterator gibi
              int id=rs.getInt("id");
              String ad=rs.getString("ad");
              String soyad=rs.getString("soyad");
              String email=rs.getString("email");

              System.out.println("id: " + id + "Ad: "+ ad + "Soyad: " + soyad + "Email: " + email);
           }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void calisanEkle(){

        try {
            statement = con.createStatement();
            String ad="Seyma";
            String soyad="orhan";
            String email="seymaorhan@gmail.com";
            String sorgu = "insert into calisanlar(ad,soyad,email) values (" +"',"+ ad + "'," + "',"  + soyad + "'," + "',"  +email +"')";

            statement.executeUpdate(sorgu);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //uzak bir sunucuysa adresini vermem gerek

    public void preparedCalisanlar(int id) {//kolay ekleme yapmak için

        String sorgu="Select * from calisanlar where id > ?  and ad like ?";

        try {
     preparedStatement=con.prepareStatement(sorgu);
            preparedStatement.setInt(1,id);//birinci soru işareti id yerine geçer. /////
            preparedStatement.setString(2,"m%");

            ResultSet rs=preparedStatement.executeQuery();
            while(rs.next()){
                String ad=rs.getString("ad");
                String soyad=rs.getString("soyad");
                String email=rs.getString("email");

                System.out.println("ad: "+ ad + "soyad " + soyad + "email " + email);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void commitverollback(){
        Scanner scanner=new Scanner(System.in);
        try {
            con.setAutoCommit(false);// sorguları normalde otomatik başlatır java. Biz burda duruma göre çalışmasını sağlamak için Aoutocommit false yapıyoruz.

            String sorgu ="Delete from calisanlar where id=3";
            String sorgu2="Update calisanlar set email='mustafamuratcoskun@gmail.com' where id=1";
            System.out.println("guncellenmeden önce....");
            calisanlariGetir();

            Statement statement = con.createStatement();
            statement.executeUpdate(sorgu);
            statement.executeUpdate(sorgu2);
            System.out.println("İslemleriniz kaydedilsin mi ? Yes/No");

            String cevap=scanner.nextLine();
            if(cevap.equals("yes")){
                con.commit();
                calisanlariGetir();
                System.out.println("Veritabanı güncellendi.");
            }
            else{
                con.rollback();
                System.out.println("Veritabanı guncellenmesi iptal edildi. ");
                calisanlariGetir();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void preparedCalisanlariGetir(){

        try {
            statement =con.createStatement();
            String sorgu="Select*from calisanlar where ad like 'M%'"; // ad like >> adı m ile başlayanlar
            ResultSet rs=statement.executeQuery(sorgu); // select oldugu icin result set

            while(rs.next()){
                System.out.println("Ad: " + rs.getString("ad"));  //verilerin içinde dolandık ve gelen adı yazdırdık
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public Baglanti(){
        //
        String url="jdbc:mysql://" + host+ ":" + port + "/"+ dbİsmi + "?useUnicode=true&characherEncoding=utf8" ;  //kodumuz güvence altına almak icin useun,codevs
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                    } catch (ClassNotFoundException e) {
                        System.out.println("Driver bulunamadı...");
                    }
        try {
            con= DriverManager.getConnection(url,kullaniciAdi,parola);
            System.out.println("Baglanti basarili...");
        } catch (SQLException e) {
            System.out.println("Baglanti basarisiz...");
        }


    }

    public static void main(String[] args) {
        Baglanti baglanti= new Baglanti();
       /* System.out.println("eklenmeden önce");
        baglanti.calisanlariGetir();
        System.out.println("*********");
        baglanti.calisanEkle();
       baglanti.calisanlariGetir(); */

       /* baglanti.calisanlariGetir();
        System.out.println("guncelleme sonrası");
        baglanti.calisanGuncelle();
        baglanti.calisanlariGetir();
        System.out.println("**********************");
        baglanti.IDBuyukUcSil();
        baglanti.calisanlariGetir(); */
        //baglanti.preparedCalisanlariGetir();
       // baglanti.preparedCalisanlar(13);
        Baglanti comroll= new Baglanti();
        comroll.commitverollback();



    }

}
