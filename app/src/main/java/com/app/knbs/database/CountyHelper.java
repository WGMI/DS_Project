package com.app.knbs.database;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Muganda Imo on 9/27/2018.
 */

public class CountyHelper {

    private String[] counties = {"Mombasa","Kwale","Kilifi","Tana River","Lamu","Taita-Taveta","Garissa","Wajir","Mandera","Marsabit","Isiolo","Meru","Tharaka-Nithi","Embu","Kitui","Machakos","Makueni","Nyandarua","Nyeri","Kirinyaga","Murang'a","Kiambu","Turkana","West Pokot","Samburu","Trans-Nzoia","Uasin Gishu","Elgeyo-Marakwet","Nandi","Baringo","Laikipia","Nakuru","Narok","Kajiado","Kericho","Bomet","Kakamega","Vihiga","Bungoma","Busia","Siaya","Kisumu","Homa Bay","Migori","Kisii","Nyamira","Nairobi"};
    private String[] sub_counties = {"Baringo Central","Baringo South","Baringo North ","Tiaty","Eldama Ravine ","Mogotio","Bomet Central","Bomet East","Chepalungu","Sotik","Konoin","Kimilili","Webuye","Sirisia","Kanduyi","Bumula ","Mt. Elgon","Matayos","Nambale","Butula","Teso North","Teso South","Funyula ","Budulangi","Marakwet East","Marakwet West","Keiyo North ","Keiyo South","Manyatta","Runyejes","Mbeere South","Mbeere North","Garissa","Balmbala","Lagdera","Dadaab","Fafi","Ijara","Kasipul","Kabondo Kasipul","Karachuonyo","Rangwe","Homa Bay","Ndhiwa","Mbita","Suba","Isiolo North ","Isiolo South","Kajiado North","Kajiado East","Kajiado Central","Kajiado South","Kajiado West","Lugari","Lukuyani","Malava","Lurambi","Lavakholo","Mumias West","Mumias East","Matungu","Butere","Khwisero","Shinyalu","Ikolomani","Kipkeilon East","Kipkeilon West","Ainamoi","Bureti","Sigowt-Soin","Belgut","Juja","Gatundu North","Gatundu South ","Thika ","Ruiru","Guthunguri","Kiambaa","Kiambu","Kabete","Kikuyu","Limuru","Lari","Kaloleni","Rabai","Kilifi North","Ganze ","Malindi ","Magarini","Kilifi South","Mwea","Gichungu","Kirinyaga Central","Ndia","Bobasi","Bonchari","South Mugirango","Bomachoge Borabu","Bomachoge Chache","Nyaribari Masaba","Nyaribare Chahe","Kitutu Chache North ","Kitutu Chache South","Kisumu West","Kisumu Central","Seme ","Nyando","Nyakach","Muhoroni","Kisumu East","Mwingi North","Mwingi West","Mwingi Central","Kitui Rural ","Kitui Central","Kitui South","Kitui West","Mutito","Msambweni","Lunga Lunga","Matuga ","Kinango","Laikipia East","Laikipia North","Laikipia West","Lamu East ","Lamu West","Masinga","Yatta","Kangundo","Matungulu","Mwala","Mavoko","Machakos","Kathiani","Mbooni ","Kilome","Kaiti","Makueni","Kibwezi East","Kibwezi West","Mandera West","Mandera North","Mandera South","Lafey","Madera East","Banissa","Tigania West","Tigania East","Igembe South","Igembe Central ","Igembe North","North Imenti","Central Imenti ","South Imenti","Buuri","Suna East","Suna West","Rongo ","Awendo","Uriri","Nyatike","Kuria West","Kuria East","Moyale","North Horr","Laisamis","Saku","Jomvu","Kisauni","Changamwe","Nyali","Likoni ","Mvita","Kangema","Mathioya","Kiharu","Kigumo","Maragwa","Kandara ","Gatanga","Westlands","Dagoretti North","Dagoretti South","Mathare ","Starehe","Kamukunji","Makadara","Embakasi East","Embakasi West","Embakasi Central","Embakasi South ","Embakasi North","Ruaraka","Kasarani","Roy Sambu","Kibra","Langata","Molo","Njoro","Naivasha","Gilgil","Kuseroi South","Kuseroi North","Subukia","Rongai","Bahati ","Nakuru Town West","Nakuru Town East","Tinderet","Aldai","Chesumei","Nandi Hills","Mosop","Emgwen","Kilgoris","Narok North","Narok South","Narok East","Narok West","Emurua Dikirr","West Mugirango","Borabu","North Mugirango","Kitutu Masaba","Kinangop","Kipipiri","Ol Kalou","Ndaragwa","Ol Jorok","Tetu ","Kieni","Mathira","Othaya","Nyeri Town","Murukweini","Samburu West","Samburu North","Samburu East","Ugenya","Ugunja","Alego Usonga","Gem","Bondo","Rarienda","Mwatete","Voi","Taveta","Wundanyi","Bura","Garsen ","Galole","Maara","Chuka/ Igambangombe","Tharaka","Kwanza","Endebess","Saboti","Kiminini","Cherengany","Loima ","Turkana North","Turkana South","Turkana Central","Turkana East","Turkana West","Soy","Turbo ","Moiben","Ainabkoi","Kesses ","Kapseret","Vihiga","Sabatia","Hamisi ","Luanda","Emuhaya","Wajir North ","Wajir East","Wajir West","Wajir South","Tarbaj","Eldas","Kapenguria","Sigor","Pokot South","Kacheliba"};

    public int getCountyId(String county){
        county = county.trim();
        int id = Arrays.asList(counties).indexOf(county);
        return id;
    }

    public int getSubCountyId(String subCounty){
        subCounty = subCounty.trim();
        int id = Arrays.asList(sub_counties).indexOf(subCounty);
        return id;
    }
}
