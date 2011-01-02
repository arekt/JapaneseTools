class Translation 
include MongoMapper::EmbeddedDocument
  key :content, String, :required => true
  key :lang,  String, :required => true
end
