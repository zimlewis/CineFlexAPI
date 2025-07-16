-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[ShowGenre] (
    [Show] UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES [dbo].[Show]([Id]),
    [Genre] UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES [dbo].[Genre]([Id]),
    PRIMARY KEY ([Show], [Genre])
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[ShowGenre];
-- +goose StatementEnd
