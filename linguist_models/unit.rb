class Unit 
  include MongoMapper::Document
  key :name, String
  key :sentence_categories, Array
  key :word_categories, Array
  key :language, String 
 
  many :assets 
  many :words do
    def without_asset
      #if fragment don't exist
      #or fragment.asset_id is nil
      self.select do |w| w.fragment == nil || w.fragment.asset_id == nil end
    end
  end
  many :sentences do
    def without_asset
      #if fragment don't exist
      #or fragment.asset_id is nil
      self.select do |s| s.fragment == nil || s.fragment.asset_id == nil end
    end
  end

  many :dialogs
end
