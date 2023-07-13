# Features
- App is feature complete according to the provided requirements and figma design
- Json data is located in assets/stock. Ne data sets can be copied into this folder but needs to follow the naming pattern
  As the name of the files are linked to the component endpoints new data sets need to follow the naming 
  scheme of stock-item-1, stock-item-1 etc. example("/stock-items/2" navigates to stock-item-2)
- Koin for dependency injection, Turbine and Truth for unit testing
- The dependencies don't require any additional setup other than allowing gradle to download and sync

# Extra
- Additional home screen which will allow users to select and navigate directly to a stock. This was initially done for testing purposes
- Koin is used to provide singletons to our view models and repositories. In order to provide a singleton
  you can use single{} or viewModel{} in the di/module file.

# Additional Information
- Fully unit tested the StockDetailsViewModel but due to time constraints I have not unit tested the domain and data layers
- Not written any unit tests for the StockViewModel screen as this was designed initially for testing only and is not part
  of the requirement