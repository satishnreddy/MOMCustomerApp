package com.mswipetech.sdk.network.util.generators;

import com.mswipetech.sdk.network.util.params.KeyGenerationParameters;

import java.util.Random;

public class BasicKeyGenerator
    
{
    KeyGenerationParameters params;

    String key[] = {"dmdE0NgUlBqvpYh8NrNgvmdE",
                          "KCu02iV+X1OYq6NxCNnS1Mg7",
                          "Rcjx+aJeLTETmogIl9AuRDoB",
                          "ji3dylHR/CP4ku5jketoaR2y",
                          "3YOiCJPtMIG/SwiBC3ZjLPML",
                  "4SgUBdbHxfv9t/0zmTzolnrH",
                  "2yDTSH0/sEjBPkyuJDjikmBW",
                  "IjQ0eY7egZpwYHUiiUv5H2P4",
                  "tMBlW4piFeB2iFIvp3Q29zRj",
                  "lWOUiTcoLspVxN1Lc4nai2uP",
                  "SVExj+1qoFGkKLeCtOJ1Sosg",
                  "1BNEsAC4Em8OzdobJgA9WxPK",
                  "6Ec8X7OPVYACE25Q92PfwZJ1",
                  "0fyrXftch2MCCP3FGBZ+PNn6",
                  "XQcL7F/FNkFJ/L+KDiRrOlbW",
                  "GgJVmGriZWTT9WAV6oK/z84P",
                  "MeFFuFmuEXFAHqfzfXwH56tI",
                  "jEP8VkAzSxXYWHg5YYjJvbTx",
                  "u9xendE3if4lNczAa9slOeEp",
                  "bqcJ+QFR6lTJNkqfCMw4Gz8t",
                  "iBlm0//Vlpn7o8n87TK+GrzD",
                  "oBDi7XSOQ9pQfZLChFl5hGHO",
                  "S/4CPHZ+vnSM1C1LLAMKqu+g",
                  "/i2oFlz71R803TOa2sCmyKkB",
                  "aU1GWaxOOwKRFDTK6zqBbWKE",
                  "/7NGn/Bj5Vflbg5zcLRubnRj",
                  "WUjpr2IWU44i8RsENyzYHadw",
                  "dUl23oOoX4Uu6hL3moMkZhET",
                  "SYUiVqonleN0zUh+xPZcs2q3",
                  "BJOrtez3CBcmBLmCxnxpmCER",
                  "JpBS74yRznIA6MhXeH04isSs",
                  "nDJxZlcqBpT6j5Ha6dSooJUY",
                  "RZbG1YW7GSBJK3RmGHUfiUBR",
                  "p4e7FA2XYgllOWd+XNTj/DVH",
                  "Jb+LdhaJVqG63nCUVbzqVGXK",
                  "ygKpd4dMokSeK2rK4C/MF/ic",
                  "jUHYnLqUP3o5o8Nq5RisGj5+",
                  "xaGBjZCopXPUWIUvP1eCeGHn",
                  "SVs0iWoBsqkRIIny7grUBe5+",
                  "7XHYXXr+DVlEsfHbbV0QwDxp",
                  "dC5VdasxKwNkHtITac3oFif5",
                  "GcUMRVOJMXsdlq36YJBiZ+QJ",
                  "e5HurnhoWGJ74Z9v6ZnSiuFk",
                  "214iRqAS8kCFa4Zcrid/Q/g+",
                  "3ethUWFh7nnOmHebKdFw90nn",
                  "rXVvKBBZrO9tE6Ct6V+dyr8/",
                  "iZtDsf0VqJg9rFPo2pV0A+6i",
                  "eO6dXollgD256nIscBkgx+FH",
                  "dmpsRt1RhsITA+28mWZt9Uw8",
                  "XWpFR9VQhH/QRowr3x0VaWl5",
                  "piUY0nQ4dp3Hl0uNOTbNd+BU",
                  "MXYBhL81VIdvFCsWSO3LSd1l",
                  "MRARp3uw0NqKep1Y4iXi9KOM",
                  "Ln+TTrPnvbl9xrWX3KpQms7D",
                  "YIwQBJmaCoTjsQYSQxYtpeKR",
                  "ltahctD5FL5NIDM1OsWllmf0",
                  "mvUmarZR2FXMBeJDnHs7BfI5",
                  "j52dSF8TFK1dRQgOgwK7zgGB",
                  "MUM3LoCemrTM57E0O13JGvrn",
                  "Q7YnrnO9/MaeGMInd4dwC2I0",
                  "8ZXf7ww9K1Beosm+QZgzFmOg",
                  "PJWwFIIEY3AZFUXodkK2hOIk",
                  "TqdP4kj0tvrw2ZxH8iwaJXny",
                  "9ppwTPUAminehXa5Z+HVTF3J",
                  "nqwBEf4oKNSEHURM4xTscpmr",
                  "guhxkDu99s3r+FTcoKTiXt5f",
                  "Kk+/muNMPbxLTyMdVaNi3VKn",
                  "GNQv0AgRJED0FYxUhXaEDMwU",
                  "RFxstS/tNsicGFyAJS0Ufok4",
                  "HiSQQNVibtUA8ye0+A9UoRSm",
                  "B94jp8wstG6WUHjpcb6u7on8",
                  "31WWt0/y4TZNxpOqQqmLTBLG",
                  "hY5gATpCzzCMDElWAKJOmCAI",
                  "3rd2a6/ZFl0lddqGQspGhcaT",
                  "+4xXwr4gv7+BLJ8Kd7EirzlR",
                  "CeDzAUTU0StQ9TP1UNOQLBPs",
                  "9ObtR4u9gfA5QVjtyW/LiYIZ",
                  "faRtZPgVjJjLrAEVMlM8eMRs",
                  "ZLI3AEOg+dx/0xznN3U4Cf3w",
                  "gVw9QfKLjwbKCFejFHNlJgv9",
                  "lgqhuZMp4aydT48Bo8bU4/Zd",
                  "7y/dHY/s27l8T0JbyN3NFOPF",
                  "V6pkE+Q1HqgVZMH5SKn3rmb6",
                  "LVtlQ2FhEBNcCI5OeSr7QYuz",
                  "fGQ25Rj+zOKVstXHP0kwnbIv",
                  "JEr4mRAGE0gar1W4iB8PnKXh",
                  "MdshcAkDWBEZ5b+Q7JR3iNs7",
                  "oGXw+E00rr+SoI+5r6Jajzfm",
                  "97u9o88mZ9ZiiK5PQY7FGVVx",
                  "sgtWUW6wYxL4bD7Cuei+P+ID",
                  "an7toGYOT9BI4IHWIVekLp04",
                  "uA127Gr0sgTIHN08on25D3I1",
                  "ZRTvnEGk0xzHq9JebWm4yJkl",
                  "u5JAFbqe15vE5A7i+/vyjjPT",
                  "BvzUe5jJWCd+0uSv0tYlZCEr",
                  "HBOP5iOyTI2PU/CFPOWK9hkg",
                  "8SnokOmuUWD5BY/g1O+hclkl",
                  "YqW/Pw3Su49mxeZpybnUnd9V",
                  "2Hk18sk5l1WIEDdq3d+3/xG/",
                  "TbUPYtWfrUcrTAAM08ddetha"};

    public void init(
        KeyGenerationParameters param)
    {
        this.params = params;
    }

    public byte[] generateKey()
    {
        byte[]  newKey = new byte[params.getStrength()];
        Random random = params.getRandom();
        
        random.nextBytes(newKey);

        return newKey;
    }

    
}
