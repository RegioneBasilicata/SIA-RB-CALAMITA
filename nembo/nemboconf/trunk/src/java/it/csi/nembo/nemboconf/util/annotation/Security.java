package it.csi.nembo.nemboconf.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.TYPE, ElementType.METHOD })
public @interface Security
{
  public static String VISUALIZZA_CATALOGO_MISURE             = "VISUALIZZA_CATALOGO_MISURE";
  public static String GESTISCI_CATALOGO_MISURE               = "GESTISCI_CATALOGO_MISURE";
  public static String VISUALIZZA_PIANO_FINANZIARIO_REGIONALE = "VISUALIZZA_PIANO_FINANZIARIO_REGIONALE";
  public static String GESTISCI_PIANO_FINANZIARIO_REGIONALE   = "GESTISCI_PIANO_FINANZIARIO_REGIONALE";
  public static String VISUALIZZA_AMBITI_TEMATICI             = "VISUALIZZA_AMBITI_TEMATICI";
  public static String GESTISCI_AMBITI_TEMATICI               = "GESTISCI_AMBITI_TEMATICI";
  public static String VISUALIZZA_PIANO_FINANZIARIO_LEADER    = "VISUALIZZA_PIANO_FINANZIARIO_LEADER";
  public static String GESTISCI_PIANO_FINANZIARIO_LEADER      = "GESTISCI_PIANO_FINANZIARIO_LEADER";
  public static String GESTISCI_CRUSCOTTO_BANDI               = "GESTISCI_CRUSCOTTO_BANDI";
  public static String MONITORAGGIO                           = "MONITORAGGIO";
  public static String GESTIONE_DICHIARAZIONE_SPESA           = "GESTIONE_DICHIARAZIONE_SPESA";

  public enum DIRITTO_ACCESSO
  {
    READ_ONLY, READ_WRITE;
  }

  String macroCDU() default "";

  long idLivello() default Long.MIN_VALUE;

  DIRITTO_ACCESSO dirittoAccessoMinimo();

  public String errorPage() default ""; // Eventuale pagina di errore per
                                        // gestione problemi particolari (es
                                        // popup)
}
