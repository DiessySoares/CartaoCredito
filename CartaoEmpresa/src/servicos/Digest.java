/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicos;

import java.security.*;
import java.math.*;
import javafx.scene.control.Alert;

public class Digest {

    public static String stringToMD5(String text) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(text.getBytes(), 0, text.length());
            return new BigInteger(1, m.digest()).toString(16);
        } catch (Exception e) {
            Dialog.Dialog(Alert.AlertType.ERROR, "Erro", "MD5 Erro");
        }
        return null;
    }

    public static boolean stringMD5Compare(String plain, String md5) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(plain.getBytes(), 0, plain.length());
            return (new BigInteger(1, m.digest()).toString(16)).equals(md5);
        } catch (Exception e) {
            Dialog.Dialog(Alert.AlertType.ERROR, "Erro", "MD5 Erro veirifcaçao");
        }
        return false;
    }

}
