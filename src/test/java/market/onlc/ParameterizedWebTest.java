package market.onlc;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

public class ParameterizedWebTest {

    @Owner("amasgutova7@gmail.com")
    @DisplayName("Поиск по инн и наименование поставщика")
    @ValueSource(strings = {"1649069856", "ООО \"Карта\"", "179443688510", "8695567747", "ПАО \"Печатка и Ъ\""})
    @ParameterizedTest(name = "Тестирование общего алгоритма поиска: {0}")
    void commonSearchTest(String testDate) {
        step("Открываем страницу", () -> {
            open("https://dev1.onlc.market/home/offer/list/");
        });
        step("Поиска по ИНН  и по наименование нажать интер", () -> {

            $("#HomeOffersForm_providerQuery").setValue(testDate).pressEnter();

        });
        step("Проверяем результат,найдено по поставщику Карта", () -> {
            Thread.sleep(9000);
            return Stream.of(
                    Arguments.of("1649069856", "Найдено: 341314"),
                    Arguments.of("179443688510", "Найдено: 3")
            );
        });
    }

    @DisplayName("Поиск по наименованию оферты ")
    @ValueSource(strings = {"кепка"})
    @ParameterizedTest(name = "Тестирование общего алгоритма")
    void commonSearchTests(String testData) {
        step("", () -> {
            open("https://dev1.onlc.market/home/offer/list/");
            $("#HomeOffersForm_query").setValue(testData).pressEnter();
            Thread.sleep(9000);

            $(".ant-list-item")
                    .shouldHave(Condition.text(testData));
        });
    }

    @DisplayName("Поиск по наименованию оферты")
    @CsvSource(value = {"кепка, Найдено: 30"})
    @ParameterizedTest(name = "Тестирование общего алгоритма поиска")
    void csvTest(String testData, String expected) {
        step("", () -> {
            open("https://dev1.onlc.market/home/offer/list/");
            $("#HomeOffersForm_query").setValue(testData).pressEnter();
            Thread.sleep(9000);
            $(".sub-top___StyledCol-sc-es4zgy-1")
                    .shouldHave(Condition.text(expected));
        });
    }
    static Stream<Arguments> commonSearchDataProvider(){
        return Stream.of(
                Arguments.of("22222", "Найдено: 0"),
                Arguments.of("страховка", "Найдено: ")
        );
    }
    @MethodSource("commonSearchDataProvider")
    @ParameterizedTest(name = "Тестирование с тестданными: {0}")
    void searchWithMethodSourceTest(String testData, String expectedResult) {
        open("https://dev1.onlc.market/home/offer/list/");
        $("#HomeOffersForm_query").setValue(testData).pressEnter();

        $(".sub-top___StyledCol-sc-es4zgy-1")
                .shouldHave(Condition.text(expectedResult));
    }
}







