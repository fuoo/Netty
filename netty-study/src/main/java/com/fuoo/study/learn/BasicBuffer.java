package com.fuoo.study.learn;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(5);

        for (int i = 0; i < 5; i++) {
            intBuffer.put(i);
        }
        System.out.println(intBuffer.toString());
        intBuffer.flip();

        while (intBuffer.hasRemaining()) {
            intBuffer.mark();
            System.out.println(intBuffer.position());
            System.out.println(intBuffer.get());
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT TR_OPTTYPE, AC_OPTTYPE,TOPCLASS,SUM(COUNT) COUNT, SUM(BALANCE) BALANCE, TYPENAME FROM ( ");
        sql.append(" SELECT /*+index(A,ACCOUNT_RECHARGE_CONFIRM) index(TRANSIN,RPT_RECHARGE_TRADENO)*/ case when (TRANSIN.TOPCLASS = '10' and TRANSIN.OPTTYPE='4' and  (A.OPTTYPE ='1090' OR A.OPTTYPE ='1101')) then  '61' ");
        sql.append(" when (TRANSIN.TOPCLASS = '10' and TRANSIN.OPTTYPE='13' and (A.OPTTYPE ='1090' OR A.OPTTYPE ='1101')) then '01' ");
        sql.append("  else TRANSIN.TOPCLASS end TOPCLASS,                                             ");
        sql.append("     to_char(TRANSIN.OPTTYPE) AS TR_OPTTYPE,                                    ");
        sql.append("      DECODE( A.OPTTYPE, '1101', '1090',  A.OPTTYPE)      AS AC_OPTTYPE,                       ");
        sql.append("     'CZ' as TYPENAME,                                                 ");
        sql.append("     COUNT(*) AS COUNT,                                                ");
        sql.append("     NVL(SUM(TRANSIN.INCMNY), 0) AS BALANCE                            ");
        sql.append("FROM TR_TRANSIN TRANSIN, AC_ACCOUNTOPT A                               ");
        sql.append("WHERE TRANSIN.TRADENO = A.TRADENO                                      ");
        sql.append(" AND A.STATE NOT IN (2, 10) AND TRANSIN.TOPCLASS != 60  ");
        sql.append("AND (");
        sql.append("    ( TRANSIN.OPTON >= TO_DATE(?, 'YYYY/MM/DD hh24:mi:ss') AND TRANSIN.OPTON < TO_DATE(?, 'YYYY/MM/DD hh24:mi:ss')  AND (TRANSIN.MPAYTYPE != '1' OR TRANSIN.MPAYTYPE IS NULL) )    ");
        sql.append("OR");
        sql.append("   ( TRANSIN.OPTON >= TO_DATE(?, 'YYYY/MM/DD hh24:mi:ss') AND TRANSIN.OPTON < TO_DATE(?, 'YYYY/MM/DD hh24:mi:ss')  AND TRANSIN.MPAYTYPE = '1' )");
        sql.append(")");
        sql.append(" group by  case when (TRANSIN.TOPCLASS = '10' and TRANSIN.OPTTYPE='4' and  (A.OPTTYPE ='1090' OR A.OPTTYPE ='1101')) then  '61'  ");
        sql.append(" when (TRANSIN.TOPCLASS = '10' and TRANSIN.OPTTYPE='13' and  (A.OPTTYPE ='1090' OR A.OPTTYPE ='1101')) then '01' else TRANSIN.TOPCLASS  end   , TRANSIN.OPTTYPE, A.OPTTYPE  ");

        //新需求 topclass=60 根据mpaytype = 1(银联) or mpaytype = 2(支付宝) or mpaytype = 3(微信) 区分开(2019-07-25)
        sql.append(" UNION ALL ");
        sql.append(" SELECT /*+index(A,ACCOUNT_RECHARGE_CONFIRM) index(TRANSIN,RPT_RECHARGE_TRADENO)*/ ");
        sql.append("  CASE WHEN TRANSIN.MPAYTYPE = '1' THEN '601'                                      ");
        sql.append("    WHEN TRANSIN.MPAYTYPE = '2' THEN '602'                                         ");
        sql.append("    WHEN TRANSIN.MPAYTYPE = '3' THEN '603'                                         ");
        sql.append("    ELSE  '601' END TOPCLASS,                                                      ");
        sql.append("  TO_CHAR(TRANSIN.OPTTYPE) AS TR_OPTTYPE,                                          ");
        sql.append("  DECODE( A.OPTTYPE, '1101', '1090',  A.OPTTYPE)      AS AC_OPTTYPE,               ");
        sql.append("  'CZ' AS TYPENAME,                                                                ");
        sql.append("  COUNT(*) AS COUNT,                                                               ");
        sql.append("  NVL(SUM(TRANSIN.INCMNY), 0) AS BALANCE                                           ");
        sql.append("   FROM TR_TRANSIN TRANSIN, AC_ACCOUNTOPT A                                        ");
        sql.append("  WHERE TRANSIN.TRADENO = A.TRADENO                                                ");
        sql.append("    AND TRANSIN.TOPCLASS = 60                                                      ");
        sql.append("    AND A.STATE NOT IN (2, 10)                                                     ");
        sql.append("AND (");
        sql.append("    ( TRANSIN.OPTON >= TO_DATE(?, 'YYYY/MM/DD hh24:mi:ss') AND TRANSIN.OPTON < TO_DATE(?, 'YYYY/MM/DD hh24:mi:ss')  AND (TRANSIN.MPAYTYPE != '1' OR TRANSIN.MPAYTYPE IS NULL) )    ");
        sql.append("OR");
        sql.append("   ( TRANSIN.OPTON >= TO_DATE(?, 'YYYY/MM/DD hh24:mi:ss') AND TRANSIN.OPTON < TO_DATE(?, 'YYYY/MM/DD hh24:mi:ss') AND  TRANSIN.MPAYTYPE = '1')  ");
        sql.append(")");
        sql.append("  GROUP BY TRANSIN.OPTTYPE, A.OPTTYPE, TRANSIN.MPAYTYPE                            ");
        sql.append("  ) GROUP BY TR_OPTTYPE, AC_OPTTYPE,TOPCLASS,TYPENAME  ");

        System.out.println(sql.toString());


    }
}
