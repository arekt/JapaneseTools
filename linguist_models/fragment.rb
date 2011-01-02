class Fragment
  include MongoMapper::EmbeddedDocument
  key :start, Integer
  key :stop,  Integer
  key :asset_id, ObjectId

  belongs_to :asset  
end
