class Word
  include MongoMapper::Document
  key :content, String, :required => true, :index => true
  key :lang, String
  key :category, String
  key :unit_id, ObjectId, :index => true
  #key :fragment, Fragment
  timestamps!

  many :translations
  belongs_to :unit
  one :fragment 

end
