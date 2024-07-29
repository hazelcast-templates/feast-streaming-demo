from datetime import timedelta
from feast import FeatureView, Entity, ValueType, Field
from feast.data_source import PushSource
from feast.infra.offline_stores.contrib.postgres_offline_store.postgres_source import PostgreSQLSource
from feast.types import Int32

# Add an entity for users
user_entity = Entity(
    name="user_id",
    description="A user that has executed a transaction or received a transaction",
    value_type=ValueType.STRING
)

user_transaction_count_7d_source = PushSource(
    name="user_transaction_count_7d",
    batch_source=PostgreSQLSource(
        table="user_transaction_count_7d",
        timestamp_field="feature_timestamp"),
)

user_transaction_count_7d_stream_fv = FeatureView(
    schema=[
        Field(name="transaction_count_7d", dtype=Int32),
    ],
    name="user_transaction_count_7d",
    entities=[user_entity],
    ttl=timedelta(weeks=1),
    online=True,
    source=user_transaction_count_7d_source,
)
