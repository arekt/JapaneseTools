require 'rubygems'
require 'mongo_mapper'
require 'pp'

module DB
  def self.database_initialize(name)
    MongoMapper.connection = Mongo::Connection.new('localhost',27017)
    MongoMapper.database = name
 end
  class Tag
      include MongoMapper::EmbeddedDocument
      key :name, String
  end


  class Word
      include MongoMapper::Document
      key :kanji, String
      key :hiragana, String
      key :romaji, String
      key :desc, String
      many :tags, :class_name => "DB::Tag"
  end

  def self.create_word(params = {})
    return false if params.empty?
    Word.create(params)
  end
end
