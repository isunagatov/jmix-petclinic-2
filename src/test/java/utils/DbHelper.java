package utils;



import pages.Pets;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class DbHelper {

    public static Connection getConnection(ReadConfig.DataBase.DB dbBySubSystem) {
        List<ReadConfig.DataBase.DB> dbConfig = ReadConfig.DataBase.getListDB();
        String DbHost = dbConfig.get(0).getDatabaseHost();
        String DbUser = dbConfig.get(0).getDatabaseUser();
        String DbPass = dbConfig.get(0).getDatabasePass();
        String DbName = dbConfig.get(0).getDatabaseName();

        String DB_URL = "jdbc:postgresql://" + DbHost + "/" + DbName;
        //System.out.println("Testing connection to PostgreSQL JDBC");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return null;
        }

        // System.out.println("PostgreSQL JDBC Driver successfully connected");
        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection(DB_URL, DbUser, DbPass);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return null;
        }

        if (connection != null) {
            String result = "Success.DB connect";
            //       System.out.println("result);

        } else {
            String result = "Failed to make connection to database";
            System.out.println("Failed to make connection to database");

        }
        return connection;
    }

    public static class PetsCheck{
        public static String getPetIdByIdentificationNumber(Pets.PetObject o) throws SQLException {
            Connection connection = getConnection(ReadConfig.DataBase.getDbBySubSystem("Petclinic"));
            PreparedStatement stmt = connection.prepareStatement(
                    "select * from petclinic_pet pp where identification_number = ?", new String[]{"id"});
            stmt.setString(1, o.getIdentificNumber());

            String petId = null;
            try {
                ResultSet rs = stmt.executeQuery();
                petId = null;
                while (rs.next()) {
                    petId = rs.getString("id");
                    System.out.println("Pet id: " + petId);
                }
                rs.close();
                stmt.close();
                return petId;
            } catch (SQLException e) {
                System.out.println("Pet id отсутствует:" + petId);
                stmt.close();
                return null;
            }
        }
        public static Pets.PetObject getPetByIdentificationNumber(Pets.PetObject o) throws SQLException {
            Connection connection = getConnection(ReadConfig.DataBase.getDbBySubSystem("Petclinic"));
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT pp.id as \"pp.id\", pp.name as \"pp.name\", pp.birthdate as \"pp.birthdate\", " +
                            "pp.type_id, pp.owner_id,pp.identification_number as \"pp.identification_number\", " +
                            "pt.name as \"pt.name\" , po.first_name as \"po.first_name\"\n" +
                            "FROM public.petclinic_pet pp \n" +
                            "left join petclinic_pet_type pt on (pp.type_id = pt.id )\n" +
                            "left join petclinic_owner po on (pp.owner_id = po.id)\n" +
                            "where pp.identification_number = ?;", new String[]{"id"});
            stmt.setString(1, o.getIdentificNumber());

            Pets.PetObject Pet = new Pets.PetObject();
            String petId = null;
            try {
                ResultSet rs = stmt.executeQuery();
                petId = null;
                while (rs.next()) {
                    Pet.setId(UUID.fromString(rs.getString("pp.id")));
                    System.out.println("Pet id: " + petId);

                    Pet.setIdentificNumber(rs.getString("pp.identification_number"));
                    System.out.println("Pet identification_number: " + Pet.getIdentificNumber());

                    Pet.setName(rs.getString("pp.name"));
                    System.out.println("Pet name: " + Pet.getName());

                    Pet.setBirthDateWeb(rs.getString("pp.birthdate"));
                    System.out.println("Pet pp.birthdate: " + Pet.getBirthDateWeb());

                    Pet.setOwner(rs.getString("po.first_name"));
                    System.out.println("po.first_name: " + Pet.getOwner());

                    Pet.setType(rs.getString("pt.name"));
                    System.out.println("pt.name: " + Pet.getType());
                }
                rs.close();
                stmt.close();
                return Pet;
            } catch (SQLException e) {
                System.out.println("Pet id отсутствует:" + petId);
                stmt.close();
                return null;
            }
        }
    }

}
