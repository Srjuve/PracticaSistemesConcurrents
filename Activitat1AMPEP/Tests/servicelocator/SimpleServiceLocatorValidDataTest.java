package servicelocator;
import org.junit.jupiter.api.BeforeEach;
import testClasses.*;

class SimpleServiceLocatorTest {
    ServiceLocator locator;
    @BeforeEach
    void initData() throws LocatorError{
        locator=new SimpleServiceLocator();
        locator.setConstant("IntValue",Integer.valueOf(1));
        locator.setConstant("StringValue","Patata");
        locator.setService("D",new FactoryD1());
        locator.setService("C",new FactoryC1());
        locator.setService("B",new FactoryB1());
        locator.setService("A",new FactoryA1());
    }
}