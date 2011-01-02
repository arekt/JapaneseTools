require 'rubygems'
require 'mongo_mapper'
require 'pp'


module DB
 def self.database_initialize(name)
    if ENV['DB_HOST']
      MongoMapper.connection = Mongo::Connection.new(ENV['DB_HOST'],27017)
      MongoMapper.database.authenticate(ENV['DB_USER'], ENV['DB_PASS'])
    else
      MongoMapper.connection = Mongo::Connection.new('localhost',27017)
    end
    MongoMapper.database = name
    require 'linguist_models/word.rb'
    require 'linguist_models/unit.rb'
    require 'linguist_models/translation.rb'
    require 'linguist_models/fragment.rb'
 end
end



if __FILE__ == $0
  DB.database_initialize('linguist2_development')
  unit = Unit.first(:name=>"MNN28")
  puts "Words: #{Word.all(:unit_id =>unit.id).map(&:content).join(' ')}"
end
