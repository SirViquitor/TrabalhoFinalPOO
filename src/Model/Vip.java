package Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Vip extends Cliente{

    public static void create(
        String rg,
        String nome
    ) throws IllegalArgumentException, SQLException {
        Cliente.create(rg, nome, TipoCliente.VIP);
    }

    /**
     * Read com ORDER BY
     * @param orderBy tipo de Ordenação
     * @return todos os registros ordenados
     */
    public static ResultSet read(String orderBy) throws SQLException {
        return Conexao.read(
                "cliente",
                "`tipo_entrada` = '" + TipoCliente.VIP + "'",
                orderBy
        );
    }
    public static ResultSet read() throws SQLException {
        return read("nome ASC");
    }
}
