package pet.vetmap.test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.apache.commons.codec.language.bm.Lang;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class SearchTest {


    @ValueSource(strings = {"Санкт-Петербург","Москва","Новосибирск"})
    @ParameterizedTest(name = "Поиск статьи про {0} в Википедии" )
    void searchCityTest(String testCity) {
        open("https://ru.wikipedia.org");
        $(".vector-search-box-input").setValue(testCity);
        $("#searchButton").click();
        $$("#firstHeading").shouldBe(CollectionCondition.itemWithText(testCity));
    }
    @CsvSource(value = {
            "Санкт-Петербург | Расположен на северо-западе страны на побережье",
            "Москва | Расположена на западе России, на реке ",
            "Новосибирск | Город является центром",

    },delimiter = '|')
    @ParameterizedTest(name = "Поиск статьи про {0} с текстом {1} в Википедии" )
    void complexSearchCityTest(String testCity,String expectedResult) {
        open("https://ru.wikipedia.org");
        $(".vector-search-box-input").setValue(testCity);
        $("#searchButton").click();
        $$("#bodyContent")
                .first()
                .shouldHave(text(expectedResult));

    }
    static Stream<Arguments> wikiSearchTest() {
        return Stream.of(
                Arguments.of("https://ru.wikipedia.org/wiki/Санкт-Петербург",List.of("Читать", "Просмотр кода", "История")),
                Arguments.of("https://ru.wikipedia.org/wiki/Москва", List.of("Читать", "Текущая версия", "Просмотр кода", "История")),
                Arguments.of("https://ru.wikipedia.org/wiki/Новосибирск", List.of("Читать", "Править", "Править код", "История"))

        );
    }

    @MethodSource()
    @ParameterizedTest(name = "Для станицы {0} отображаются кнопки меню {1}")
    void wikiSearchTest(String url,List<String> resultSearch) {
        open(url);
        $$("#p-views li").shouldHave(CollectionCondition.textsInAnyOrder(resultSearch));
    }

}



